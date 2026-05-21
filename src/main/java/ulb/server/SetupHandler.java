package ulb.server;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.exceptions.*;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.player.PlayerMapper;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.MultiBattleSession;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Inventory;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.*;
import ulb.service.strategy.AI;
import ulb.service.strategy.StrategyRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handles game session setup, including player registration, login, and initialization of game modes
 */
public class SetupHandler {
	private final AccountService accountService;
	private final ItemService itemService;
	private final InventoryService inventoryService;
	private final BugemonService bugemonService;
	private final TeamService teamService;
	private final TowerSaveService towerSaveService;
	private final MultiBattleService multiBattleService;
	ClientHandler clientHandler;

	public SetupHandler(ClientHandler clientHandler, AccountService accountService, ItemService itemService,
						InventoryService inventoryService, BugemonService bugemonService, TeamService teamService,
						TowerSaveService towerSaveService, MultiBattleService multiBattleService) {
		this.clientHandler = clientHandler;
		this.accountService = accountService;
		this.itemService = itemService;
		this.inventoryService = inventoryService;
		this.bugemonService = bugemonService;
		this.teamService = teamService;
		this.towerSaveService = towerSaveService;
		this.multiBattleService = multiBattleService;
	}

	/**
	 * Sets up a multiplayer battle session for the connected player against the given opponent,
	 * assigns the player's team, and starts the battle if both participants are ready
	 *
	 * @param opponent the opponent player
	 * @param bugemons the list of bugemons in the player's team
	 * @throws DataAccessException if the multiplayer session cannot be initialized
	 */
	public void setupMultiBattle(PlayerDTO opponent, List<BugemonDTO> bugemons) throws DataAccessException {
		Player player = clientHandler.getPlayer();

		if (player.getUserId().isEmpty()) {
			clientHandler.sendErrorMessage("The player is not register");
			return;
		}

		Team team = makeTeam(bugemons);
		clientHandler.setTeam(team);

		MultiBattleSession battle = this.multiBattleService.getMultiBattle(player.getUserId().get(),
				opponent.getUserId());
		battle.getParticipant(player.getUserId().get()).setTeam(team);

		try {
			if (battle.isReady()) {
				battle.start(accountService);
			}
		} catch (GameException e) {
			clientHandler.sendErrorMessage("fail to start the game : " + e.getMessage());
			return;
		}

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Creates a Team instance from a list of BugemonDTO instances.
	 *
	 * @param bugemons The Bugemons of the team
	 * @return The Team instance
	 */
	private static Team makeTeam(List<BugemonDTO> bugemons) throws DataAccessException {
		List<Bugemon> entities = new ArrayList<>();
		for (BugemonDTO dto : bugemons) {
			Bugemon entity = BugemonMapper.toEntity(dto);
			entities.add(entity);
		}

		return new Team(entities);
	}

	/**
	 * Registers a new player or logs in an existing one
	 *
	 * @param playerRegisterDTO the registration or login credentials
	 * @param isLogin {@code true} to log in an existing account, {@code false} to register a new one
	 */
	public void registerPlayer(PlayerRegisterDTO playerRegisterDTO, boolean isLogin) {
		String username = playerRegisterDTO.username();
		String password = playerRegisterDTO.password();

		try {
			if (isLogin) {
				accountService.login(username, password);
			} else {
				accountService.register(username, password);
			}

			Player player = buildPlayer(playerRegisterDTO, isLogin);
			clientHandler.setPlayer(player);
			clientHandler.sendSuccessMessage();
		} catch (UserAlreadyExistsException e) {
			clientHandler.sendErrorMessage("Username already taken");
		} catch (EntityNotFoundException e) {
			clientHandler.sendErrorMessage("User not found");
		} catch (InvalidCredentialsException e) {
			clientHandler.sendErrorMessage("Wrong password");
		} catch (LoadException e) {
			clientHandler.sendErrorMessage("Internal server error");
		}
	}

	/**
	 * Builds a Player instance from registration info, loads or creates the player's inventory
	 * depending on whether they are logging in or registering.
	 *
	 * @param dto the player's registration or login data
	 * @param isLogin {@code true} if the player is logging in, {@code false} if registering
	 * @return the constructed Player
	 * @throws LoadException if the inventory cannot be loaded or created
	 * @throws EntityNotFoundException if the player account cannot be found
	 */
	private Player buildPlayer(PlayerRegisterDTO dto, boolean isLogin) throws LoadException, EntityNotFoundException {
		String username = dto.username();

		Inventory inventory;
		int userId = accountService.getUserId(username);
		if (isLogin) {
			inventory = inventoryService.getInventoryFromDatabase(userId);
		} else {
			inventory = itemService.createStarterInventory();
			inventoryService.insertInventory(inventory, userId);
		}

		return PlayerMapper.toEntity(dto, inventory, userId);
	}

	/**
	 * Initializes a normal (classic or auto) battle mode for the connected player,
	 * generates a random opponent team and starts an AI-controlled opponent thread
	 *
	 * @throws LoadException if the starter inventory cannot be created
	 */
	public void setupNormalMode() throws LoadException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();
		TowerManager towerManager = clientHandler.getTowerManager();
		boolean isGameTower = clientHandler.isGameTower();

		if (player == null) {
			clientHandler.sendErrorMessage("Player not initialized !");
			return;
		}

		if (battle != null || towerManager != null || isGameTower) {
			clientHandler.resetGameSessionState();
		}

		Team teamB;

		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(player.getTeam(), bugemonService);
		} catch (Exception e) {
			clientHandler.sendErrorMessage(e.getMessage());
			return;
		}

		Inventory starterInventory = itemService.createStarterInventory();
		// resets the inventory for each battle
		player.setInventory(starterInventory);
		inventoryService.updateInventory(starterInventory, player);
		battle = new Battle(player.getTeam(), teamB, player, new Player("PlayerB", -1, starterInventory));
		clientHandler.setBattle(battle);
		clientHandler.setTeamLabel(Battle.ParticipantLabel.TEAM_A);
		clientHandler.setGameMode(false);

		Thread opponentBot = new AI(battle, new StrategyRandom());
		opponentBot.start();
		clientHandler.clearPendingLevelUpState();

		clientHandler.sendSuccessMessage();
	}

	/**
	 * Sets up the player's active team from a list of bugemon DTOs
	 *
	 * @param bugemons the bugemons to include in the team
	 * @throws DataAccessException if a bugemon instance cannot be created
	 */
	public void setupTeam(List<BugemonDTO> bugemons) throws DataAccessException {
		Team team = new Team();

		for (BugemonDTO bugemonDTO : bugemons) {
			if (!team.add(BugemonMapper.toEntity(bugemonDTO))) {
				clientHandler.sendErrorMessage("Invalid Team");
			}
		}

		clientHandler.setTeam(team);
		clientHandler.sendSuccessMessage();
	}

	/**
	 * Initializes tower mode for the connected player, either starting a new tower run
	 * or continuing a saved one
	 *
	 * @param isNewTower {@code true} to start a new tower run, {@code false} to continue a saved one
	 * @throws DataAccessException if the tower cannot be initialized or the team cannot be loaded
	 */
	public void setupTowerMode(boolean isNewTower) throws DataAccessException {
		Player player = clientHandler.getPlayer();

		if (!isNewTower) {

			try {
				Optional<Team> playerTeam = teamService.getTowerTeam(player);
				if (playerTeam.isPresent()) {
					player.setTeam(playerTeam.get());
				}
			} catch (Exception e) {
				clientHandler.sendErrorMessage("Cannot get tower team");
				return;
			}
		} else { // resets the inventory if started a new tower
			Inventory starterInventory = itemService.createStarterInventory();
			player.setInventory(starterInventory);
			inventoryService.updateInventory(starterInventory, player);
		}

		Battle battle = clientHandler.getBattle();
		TowerManager towerManager = clientHandler.getTowerManager();
		boolean isGameTower = clientHandler.isGameTower();

		if (battle != null || towerManager != null || isGameTower) {
			clientHandler.resetGameSessionState();
		}
		try {
			towerManager = new TowerManager(player, isNewTower, bugemonService, itemService, teamService,
					towerSaveService);
		} catch (Exception e) {
			clientHandler.sendErrorMessage("The tower cannot be initialized");
			return;
		}
		clientHandler.setTowerManager(towerManager);

		clientHandler.setBattle(towerManager.getCurrentBattle());
		clientHandler.setTeamLabel(Battle.ParticipantLabel.TEAM_A);
		clientHandler.setGameMode(true);
		clientHandler.clearPendingLevelUpState();

		clientHandler.sendSuccessMessage();
	}
}
