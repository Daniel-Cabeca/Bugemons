package ulb.server;

import java.util.ArrayList;
import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.message.clientToServer.ConfirmTeamMultiMessage;
import ulb.message.clientToServer.RegisterMessage;
import ulb.message.clientToServer.SetUpNormalModeMessage;
import ulb.message.clientToServer.SetUpTeamMessage;
import ulb.message.clientToServer.SetUpTowerModeMessage;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.MultiBattleSession;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Inventory;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.AccountService;
import ulb.service.BugemonService;
import ulb.service.InventoryService;
import ulb.service.ItemService;
import ulb.service.MultiBattleService;
import ulb.service.TeamService;
import ulb.service.TowerSaveService;
import ulb.service.strategy.AI;
import ulb.service.strategy.StrategyRandom;

public class SetupHandler {
    ClientHandler clientHandler;
    private final AccountService accountService;
    private final ItemService itemService;
    private final InventoryService inventoryService;
    private final BugemonService bugemonService;
	private final TeamService teamService;
	private final TowerSaveService towerSaveService;
	private final MultiBattleService multiBattleService;

    public SetupHandler(ClientHandler clientHandler, AccountService accountService, ItemService itemService, InventoryService inventoryService, BugemonService bugemonService, TeamService teamService, TowerSaveService towerSaveService, MultiBattleService multiBattleService) {
        this.clientHandler = clientHandler;
        this.accountService = accountService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.bugemonService = bugemonService;
		this.teamService = teamService;
		this.towerSaveService = towerSaveService;
		this.multiBattleService = multiBattleService;
    }


    private Player buildPlayer(PlayerRegisterDTO dto, boolean isLogin) {
        String username = dto.username();

        Inventory inventory;
        if (isLogin) {
            inventory = inventoryService.getInventoryFromDatabase(username);
        } else {
            inventory = itemService.createStarterInventory();
            inventoryService.insertInventory(inventory, username);
        }

        int userId = accountService.getUserId(username);

        return PlayerMapper.toEntity(dto, inventory, userId);
    }

	public void handle(ConfirmTeamMultiMessage message) {
		Player player = clientHandler.getPlayer();
		PlayerDTO opponent = message.getOpponent();
		Team team = makeTeam(message.getTeam());

		MultiBattleSession battle = this.multiBattleService.getMultiBattle(player.getUserId(), opponent.getUserId());
		battle.getParticipant(player.getUserId()).setTeam(team);

		if (battle.isReady()) {
			battle.start(accountService);
		}

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Creates a Team instance from a list of BugemonDTO instances.
	 *
	 * @param bugemons The Bugemons of the team
	 * @return The Team instance
	 */
	private static Team makeTeam(List<BugemonDTO> bugemons) {
		List<Bugemon> entities = new ArrayList<>();
		for (BugemonDTO dto: bugemons) {
			Bugemon entity = BugemonMapper.toEntity(dto);
			entities.add(entity);
		}

		return new Team(entities);
	}

	public void handle(RegisterMessage message){
		boolean success;
		String username = message.getPlayer().username();
		String password = message.getPlayer().password();

		if (message.isLogin()) {
			success = accountService.login(username, password);
		}
		else {
			success = accountService.register(username, password);
		}
		if (success) {
			Player player = buildPlayer(message.getPlayer(), message.isLogin());
            clientHandler.setPlayer(player);
			clientHandler.sendSuccessMessage();
		} else {
			clientHandler.sendErrorMessage("Register failed");
		}
	}

    public void handle(SetUpNormalModeMessage message){
        Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        TowerManager towerManager = clientHandler.getTowerManager();
        boolean isGameTower = clientHandler.isGameTower();

		if (player == null){
			clientHandler.sendErrorMessage("Player not initialized !");
			return;
		}

		if (battle != null || towerManager != null || isGameTower) {
			clientHandler.resetGameSessionState();
		}

		Team teamB;

		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(player.getTeam(), bugemonService);
		} catch (Exception e){
			clientHandler.sendErrorMessage(e.getMessage());
			return;
		}

		Inventory playerBInventory = itemService.createStarterInventory();
        battle = new Battle(player.getTeam(), teamB, player, new Player("PlayerB", -1, playerBInventory));
		clientHandler.setBattle(battle);
		clientHandler.setTeamLabel(Battle.ParticipantLabel.TEAM_A);
        clientHandler.setGameMode(false);

		Thread opponentBot = new AI(battle, new StrategyRandom());
        clientHandler.setOpponentBot(opponentBot);
		opponentBot.start();
		clientHandler.clearPendingLevelUpState();

		clientHandler.sendSuccessMessage();
	}

	public void handle(SetUpTeamMessage message){
		Team team = new Team();

		for (BugemonDTO bugemonDTO : message.getTeam()){
			if (!team.add(BugemonMapper.toEntity(bugemonDTO))){
				clientHandler.sendErrorMessage("Invalid Team");
			}
		}

		clientHandler.setTeam(team);
		clientHandler.sendSuccessMessage();
	}

    public void handle(SetUpTowerModeMessage message){
		boolean setupNewTower = message.isNewTower();
        Player player = clientHandler.getPlayer();

		if (!setupNewTower){
			player.setTeam(teamService.getTowerTeam(player.getUsername()));
		}

        Battle battle = clientHandler.getBattle();
        TowerManager towerManager = clientHandler.getTowerManager();
        boolean isGameTower = clientHandler.isGameTower();

		if (battle != null || towerManager != null || isGameTower) {
			clientHandler.resetGameSessionState();
		}
		towerManager = new TowerManager(player, setupNewTower, bugemonService, itemService, teamService, towerSaveService);
		clientHandler.setTowerManager(towerManager);

		clientHandler.setBattle(towerManager.getCurrentBattle());
		clientHandler.setTeamLabel(Battle.ParticipantLabel.TEAM_A);
		clientHandler.setGameMode(true);
		clientHandler.clearPendingLevelUpState();

		clientHandler.sendSuccessMessage();
	}
}
