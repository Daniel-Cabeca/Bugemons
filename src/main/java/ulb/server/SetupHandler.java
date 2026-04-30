package ulb.server;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.message.clientToServer.RegisterMessage;
import ulb.message.clientToServer.SetUpNormalModeMessage;
import ulb.message.clientToServer.SetUpTeamMessage;
import ulb.message.clientToServer.SetUpTowerModeMessage;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.item.Inventory;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.AccountService;
import ulb.service.BugemonService;
import ulb.service.InventoryService;
import ulb.service.ItemService;
import ulb.service.strategy.AI;
import ulb.service.strategy.StrategyRandom;

public class SetupHandler {
    ClientHandler clientHandler;
    private final AccountService accountService;
    private final ItemService itemService;
    private final InventoryService inventoryService;
    private final BugemonService bugemonService;

    public SetupHandler(ClientHandler clientHandler, AccountService accountService, ItemService itemService, InventoryService inventoryService, BugemonService bugemonService) {
        this.clientHandler = clientHandler;
        this.accountService = accountService;
        this.itemService = itemService;
        this.inventoryService = inventoryService;
        this.bugemonService = bugemonService;
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
        Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        TowerManager towerManager = clientHandler.getTowerManager();
        boolean isGameTower = clientHandler.isGameTower();

		if (battle != null || towerManager != null || isGameTower) {
			clientHandler.resetGameSessionState();
		}
        towerManager = new TowerManager(player, bugemonService, itemService);
		clientHandler.setTowerManager(towerManager);
		clientHandler.setBattle(towerManager.getCurrentBattle());
		clientHandler.setTeamLabel(Battle.ParticipantLabel.TEAM_A);
		clientHandler.setGameMode(true);
		clientHandler.clearPendingLevelUpState();

		clientHandler.sendSuccessMessage();
	}
}
