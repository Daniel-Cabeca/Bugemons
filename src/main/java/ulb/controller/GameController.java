package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ulb.communication.Client;
import ulb.communication.Message;
import ulb.communication.Server;
import ulb.communication.types.*;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.controller.strategy.StrategyRandom;
import ulb.controller.towerManager.FloorManager;
import ulb.controller.towerManager.RoomManager;
import ulb.controller.towerManager.TowerManager;
import ulb.controller.windows.BattleEndController;
import ulb.controller.windows.ModeController;
import ulb.model.ability.Ability;
import ulb.model.battle.BattleState;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Item;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.model.reward.Reward;
import ulb.model.reward.RewardType;
import ulb.repository.*;
import ulb.view.WindowPath;
import ulb.view.windows.LevelUpWindow;
import ulb.view.windows.ModeWindow;
import ulb.view.windows.NextRoomWindow;
import ulb.view.windows.Window;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import ulb.service.AbilityService;
import ulb.service.BugemonService;
import ulb.service.ItemService;
import ulb.service.AccountService;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInitializer;
import ulb.repository.database.AccountDatabaseRepository;
import ulb.repository.database.BugemonSpeciesDatabaseRepository;
import ulb.repository.database.ItemDatabaseRepository;
import ulb.repository.json.AbilityJsonRepository;
import ulb.repository.json.InventoryJsonRepository;
import ulb.repository.json.ItemJsonRepository;


public class GameController extends Application implements TeamController.Listener,
BattleModeController.Listener, NextRoomController.Listener, ChooseBugemonController.Listener,
BattleWindowController.Listener, RegisterController.Listener , LevelUpController.Listener, FloorRewardController.Listener,
AttackReplacementController.Listener {

	private Player player;
	private TowerManager towerModeTowerManager;
	private BattleController normalModeBattleController;
	private Stage stage;

	private GameMode gameMode;
	private final Deque<Bugemon> pendingLevelUpBugemons = new ArrayDeque<>();
	private BattleController pendingRewardBattleController;
	private boolean rewardSequenceReturnsToNextRoom;
	private int pendingBattleXP;
	private boolean fledBattle = false;
	private boolean pendingVictory;
	private int pendingTotalXP;

	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 8080;

	private TeamController teamController;
	private ModeController modeController;
	private BattleModeController battleModeController;
	private NextRoomController nextRoomController;
	private FloorRewardController floorRewardController;
	private ChooseBugemonController chooseBugemonController;
	private BattleWindowController battleWindowController;
	private RegisterController registerController;
	private LevelUpController levelUpController;
	private AttackReplacementController attackReplacementController;
	private BattleEndController battleEndController;
	private FloorRewardController.RewardChoice pendingFloorRewardChoice;

	private AbilityService abilityService;
	private BugemonService bugemonService;
	private ItemService itemService;
	private AccountService accountService;

	public static void main(String[] args) {
		try {
			if (args.length == 0) {
				launch(args);
			} else if ("--client".equals(args[0])) {
				Client client = new Client(SERVER_IP, SERVER_PORT);

				client.sendMessage(new ConnectMessage("Bonjour server !"));

				Message message = client.receiveMessage();
				if (message instanceof ConnectMessage connectMessage) {
					System.out.println("message reçu du serveur : " + connectMessage.getConnectMessage());
				}

				client.closeSocket();
			} else if ("--server".equals(args[0])) {
				Server server = new Server(SERVER_PORT);
				server.start();
			} else {
				System.err.println("Unknown arguments.");
			}
		} catch (Exception e) {
			System.err.println("Uncaught error.");
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		setPlayer(new Player("Player", this.getItemService()));
		this.stage = primaryStage;

		Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");

		registerController = new RegisterController(stage, this, this.getAccountService());
		registerController.show();

		if (primaryStage.getScene() != null) {
			String stylesheet = getClass().getResource("/styles/global.css").toExternalForm();
			if (!primaryStage.getScene().getStylesheets().contains(stylesheet)) {
				primaryStage.getScene().getStylesheets().add(stylesheet);
			}
		}

		primaryStage.show();
	}

	/**
	 * Loads the requested FXML view and installs it in the primary stage.
	 * GameController now owns the application routing instead of delegating it to ViewManager.
	 *
	 * @param windowFxmlPath the path of the FXML file to display
	 */
	public void switchWindow(String windowFxmlPath) {
		if (stage == null) {
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(windowFxmlPath));
			Parent root = loader.load();

			Object controller = loader.getController();
			if (controller instanceof Window window) {
				window.setGameController(this);
				window.onLoad();
			}

			// temporary fix until all windows/controllers are mvc
			if (controller instanceof ModeWindow modeWindow) {
				modeWindow.setListener(modeController);
			}
			if (controller instanceof NextRoomWindow nextRoomWindow) {
				nextRoomWindow.setViewListener(nextRoomController);
			}
			if (controller instanceof LevelUpWindow levelUpWindow){
				levelUpWindow.setViewListener(levelUpController);
			}
			if (stage.getScene() == null) {
				stage.setScene(new Scene(root));
			} else {
				stage.getScene().setRoot(root);
			}
		} catch (IOException e) {
			System.err.println("Error: Could not load window " + windowFxmlPath);
			e.printStackTrace();
		}
	}

	public boolean hasFledBattle() { return fledBattle; }
	public void resetFledBattle() { fledBattle = false; }

	public GameController() {
		this.loadServices();
	}

	private void loadServices() {
		Database database = DatabaseInitializer.prepareDefaultDatabase();

		AbilityRepository abilityRepository = new AbilityJsonRepository();
		this.abilityService = new AbilityService(abilityRepository);

		BugemonSpeciesRepository bugemonSpeciesRepository = new BugemonSpeciesDatabaseRepository(database);
		this.bugemonService = new BugemonService(bugemonSpeciesRepository);

		ItemRepository itemRepository = new ItemDatabaseRepository(database);
		InventoryRepository inventoryRepository = new InventoryJsonRepository(new ItemJsonRepository());
		this.itemService = new ItemService(itemRepository, inventoryRepository);

		AccountRepository accountRepository = new AccountDatabaseRepository(database);
		this.accountService = new AccountService(accountRepository);
	}

	public void setPlayer(Player player) {this.player = player;}

	public Player getPlayer() {return this.player;}

	public Team getTeam() {return this.player.getTeam();}

	public AbilityService getAbilityService() { return this.abilityService; }
	public BugemonService getBugemonService() { return this.bugemonService; }
	public ItemService getItemService() { return this.itemService; }
	public AccountService getAccountService() { return this.accountService; }


	/**
	 * Setups the right settings for the normal mode
	 */
	public void setupNormalMode(){
		Team playerTeam = player.getTeam();
		Team opponentTeam = new Team();
		try{
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam, this.getBugemonService());
		}catch(Exception e){
			System.err.println(e);
		}
		Battle battle = new Battle(playerTeam, opponentTeam, player, new Player(this.getItemService()));
		this.normalModeBattleController = new BattleController(player, battle, ParticipantLabel.TEAM_A, this.getItemService());
		StrategyRandom strategyRandom = new StrategyRandom(battle, this.getItemService());
		Thread thread = new Thread(strategyRandom);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Setups the right settings for the tower mode
	 */
	public void setupTowerMode(){
		towerModeTowerManager = new TowerManager(this.getPlayer(), this.getBugemonService(), this.getItemService());
	}


	/**
	 * Sets up and switches to the LevelUpWindow if there are Bugemons who leveled up
	 *
	 * @param battleController the battle controller that holds reward information
	 * @param returnToNextRoom if true, returns to the next tower room after the sequence, otherwise shows the battle end screen
	 * @return true if the sequence was started, false if no rewards are pending
	 */
	public boolean startLevelUpSequenceIfNeeded(BattleController battleController, boolean returnToNextRoom, ActionEvent event) {
		pendingLevelUpBugemons.clear();

		for (Bugemon bugemon : player.getTeam().getMembers()) {
			if (bugemon.getRemainingReward() > 0) {
				pendingLevelUpBugemons.addLast(bugemon);
			}
		}

		if (pendingLevelUpBugemons.isEmpty()) {
			return false;
		}

		this.pendingRewardBattleController = battleController;
		this.rewardSequenceReturnsToNextRoom = returnToNextRoom;
		this.pendingBattleXP = battleController.getTotalXP();
		levelUpController = new LevelUpController(stage, this);
		try {
			levelUpController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// switchWindow(WindowPath.LEVEL_UP);
		return true;
	}

	/**
	 * Applies the chosen reward to the current pending Bugémon and advances the level-up sequence
	 *
	 * @param reward the reward chosen by the player
	 */
	public void handleLevelUpRewardChoice(Reward reward, ActionEvent event) {
		Bugemon currentBugemon = pendingLevelUpBugemons.peekFirst();
		if (currentBugemon == null || pendingRewardBattleController == null) {
			return;
		}

		boolean rewardApplied = pendingRewardBattleController.applyReward(currentBugemon, reward);
		if (!rewardApplied) {
			return;
		}

		while (!pendingLevelUpBugemons.isEmpty() && pendingLevelUpBugemons.peekFirst().getRemainingReward() <= 0) {
			pendingLevelUpBugemons.removeFirst();
		}

		if (!pendingLevelUpBugemons.isEmpty()) {
			switchWindow(WindowPath.LEVEL_UP);
			return;
		}

		boolean returnToNextRoom = rewardSequenceReturnsToNextRoom;
		int totalXP = pendingBattleXP;
		clearPendingLevelUpState();
		if (returnToNextRoom) {
			switchToNextRoomWindow();
		} else {
			handleBattleEnd(true, totalXP);
		}
	}

	/**
	 * Resets all state related to a pending level-up sequence
	 */
	private void clearPendingLevelUpState() {
		pendingLevelUpBugemons.clear();
		pendingRewardBattleController = null;
		rewardSequenceReturnsToNextRoom = false;
		pendingBattleXP = 0;
	}

	// Stores the battle result and switches to BattleEndWindow
	private void handleBattleEnd(boolean victory, int totalXP) {
		pendingVictory = victory;
		pendingTotalXP = totalXP;
		if (battleEndController == null) {
			battleEndController = new BattleEndController(stage, modeController);
		}
		battleEndController.show(victory, totalXP);
	}

	// Handles fleeing from a tower battle: restores HP and resets the room
	private void handleTowerFlee() {
		for (Bugemon b : player.getTeam().getMembers()) {
			b.getFightStats().setHp(b.getBaseStats().getHp());
		}
		towerModeTowerManager.getCurrentFloorManager().rewindRoom();
		fledBattle = true;
	}

	/**
	 * Handles each room when in tower mode: switches to the right window and initializes its content
	 */
	public void handleTower()  {

		towerModeTowerManager.getCurrentFloorManager().nextRoom();
		towerModeTowerManager.nextFloor();

		if (!towerModeTowerManager.isTowerCompleted()) {
			FloorManager floorManager = towerModeTowerManager.getCurrentFloorManager();

			RoomManager roomManager = floorManager.getCurrentRoomManager();
			Room currentRoom = roomManager.getRoom();
			RoomType type = currentRoom.getRoomType();

			switch (type) {
				case BATTLE, BOSS:
					switchToBattleWindow();
					break;

				case REWARD:
					if (floorRewardController == null) {
						floorRewardController = new FloorRewardController(stage, this, this);
					}
					try {
						floorRewardController.show();
					} catch (Exception e) {
						e.printStackTrace();
					}
					break;

				default:
					break;
			}
			roomManager.setRoomCompleted(true);
		} else {
			handleBattleEnd(true, 0);
		}
	}

	/**
	 * Battle controller for executing one player action (item, swap, or ability).
	 *
	 * @return tower battle from towerBattleControllerForMessages when in tower mode, otherwise normalModeBattleController
	 */
	private BattleController battleControllerForManualTurn() {
		if (gameMode == GameMode.TOWER && towerModeTowerManager != null) {
			return towerModeTowerManager.getCurrentBattleController();
		}
		return normalModeBattleController;
	}

	public TowerManager getTowerManager() {
		return towerModeTowerManager;
	}

	/**
	 * Handles a message received from a JavaFX window.
	 *
	 * @param m the message emitted by the current screen
	 * @return the response message to send back to the screen when needed
	 */
	public Message handleMessage(Message m) {
		return m.handle(this);
    }

	public Message applyOn(AutoTurnRequestMessage m){
		StrategyRandom strategyRandom = new StrategyRandom(normalModeBattleController);
		BattleState state = strategyRandom.playAutoTurn();
		return new AutoTurnResponseMessage(state);
	}

	public Message applyOn(SwitchWindowMessage m){
		switchWindow(m.getSwitchWindow());
		return null;
	}

	public Message applyOn(SetupGameModeMessage m){
		handleSetupGameModeMessage(m);
		return null;
	}

	public Message applyOn(TowerFleeMessage m){
		handleTowerFlee();
		switchToNextRoomWindow();
		return null;
	}

	public Message applyOn(TowerNextRoomMessage m){
		handleTower();
		return null;
	}

	public Message applyOn(GetInfoMessage m){
		return handleGetInfoMessage(m);
	}

	public Message applyOn(UseItemRequestMessage m){
		return handleUseItemMessage(m);
	}

	public Message applyOn(SwapRequestMessage m){
		return handleSwapMessage(m);
	}

	public Message applyOn(UseAbilityRequestMessage m){
		return handleUseAbilityMessage(m);
	}

	public Message applyOn(BattleEndCheckMessage m){
		handleBattleEndCheckMessage(m);
		return null;
	}

	public Message applyOn(LevelUpMessage m){
		LevelUpMessage levelUpMessage = (LevelUpMessage) m;
		handleLevelUpRewardChoice(levelUpMessage.getReward(), levelUpMessage.getEvent());
		return null;
	}

	public Message applyOn(ReceiveObjectRewardMessage m){
		player.getInventory().addItem(this.getItemService().getRandomItem(), 1);
		switchToNextRoomWindow();
		return null;
	}

	public Message applyOn(Message m){
		return null;
	}


	/**
	 * Sets up the game based on the chosen game mode
	 *
	 * @param m the message containing the chosen game mode
	 */
	private void handleSetupGameModeMessage(SetupGameModeMessage m) {
		gameMode = m.getGameMode();
		switch (gameMode) {
			case AUTO, CONTROLLED:
				setupNormalMode();
				break;
			case TOWER:
				setupTowerMode();
		}
	}

	/**
	 * Checks what information was requested by the active window.
	 *
	 * @param m the information request message emitted by the view
	 * @return the response containing the requested information for the view
	 */
	private Message handleGetInfoMessage(GetInfoMessage m) {
		Message answer = null;
		switch (m.getType()){
			case SETUP_GAME:
				if (gameMode == GameMode.TOWER) {
					answer = new SetupGameModeMessage(gameMode, getTeam(), player.getInventory(), towerModeTowerManager.getCurrentBattleController());
				} else {
					answer = new SetupGameModeMessage(gameMode, getTeam(), player.getInventory(), normalModeBattleController);
				}
				break;
			case REWARD_PLACE:
				 answer = new RewardPlaceMessage(towerModeTowerManager.getFloorNumber(), towerModeTowerManager.getCurrentRoomIndex());
				 break;
			case LEVEL_UP:
				Bugemon currentBugemon = pendingLevelUpBugemons.peekFirst();
				if (currentBugemon != null && pendingRewardBattleController != null) {
					answer = new LevelUpMessage(pendingLevelUpBugemons.peekFirst(), pendingRewardBattleController.getRewards(currentBugemon));
				}
				break;
			case BATTLE_END:
				answer = new BattleEndInfoMessage(pendingVictory, pendingTotalXP);
				break;
		}
		return answer;
	}

	/**
	 * Uses the item as requested by the message.
	 *
	 * @param m the message containing the item to use
	 * @return the response containing the current battle state
	 */
	private Message handleUseItemMessage(UseItemRequestMessage m) {
		Message response = null;
		Item item = m.getItem();
		BattleController battleController = battleControllerForManualTurn();
		if (battleController != null && item != null) {
			battleController.useAction(new UseItem(item));
			response = new AutoTurnResponseMessage(battleController.getState());
		}
		return response;
	}

	/**
	 * Swaps the active Bugemon to a new one.
	 *
	 * @param m the message containing the bugemon to switch to
	 * @return the response containing the current battle state
	 */
	private Message handleSwapMessage(SwapRequestMessage m) {
		Message response = null;
		Bugemon bugemon = m.getBugemon();
		BattleController battleController = battleControllerForManualTurn();
		if (battleController != null && bugemon != null) {
			battleController.useAction(new Swap(bugemon));
			response = new AutoTurnResponseMessage(battleController.getState());
		}
		return response;
	}

	/**
	 * Uses the ability as requested by the message.
	 *
	 * @param m the message containing the ability to use
	 * @return the response containing the current battle state
	 */
	private Message handleUseAbilityMessage(UseAbilityRequestMessage m) {
		Message response = null;
		Ability ability = m.getAbility();
		BattleController battleController = battleControllerForManualTurn();
		if (battleController != null && ability != null) {
			battleController.useAction(new UseAbility(ability));
			response = new AutoTurnResponseMessage(battleController.getState());
		}
		return response;
	}

	/**
	 * Handles the BattleEndCheckMessage by switching to the right window based on the battle result and the game mode.
	 *
	 * @param m the received BattleEndCheckMessage
	 */
	private void handleBattleEndCheckMessage(BattleEndCheckMessage m) {
        BattleState state = m.getBattleState();
		ActionEvent event = m.getActionEvent();

		if (event == null) return;

		BattleController battleController;
		if (gameMode == GameMode.TOWER && towerModeTowerManager != null) {
			battleController = towerModeTowerManager.getCurrentBattleController();
		} else {
			battleController = normalModeBattleController;
		}

		if (battleController == null) return;

		if (state == BattleState.WON) {
			battleController.resetFighter();
			boolean isTower = gameMode == GameMode.TOWER;
			if (!startLevelUpSequenceIfNeeded(battleController, isTower, event)) {
				if (!isTower) {
					int xp = normalModeBattleController != null ? normalModeBattleController.getTotalXP() : 0;
					handleBattleEnd(true, xp);
				} else {
					switchToNextRoomWindow();
				}
			}
		} else if (state == BattleState.LOST) {
			battleController.resetFighter();
			int xp = normalModeBattleController != null ? normalModeBattleController.getTotalXP() : 0;
			handleBattleEnd(false, xp);
		}
	}

	@Override
	public void onTeamConfirmed() {
		battleModeController = new BattleModeController(stage, this, getTeam());
		try {
			battleModeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onReturn() {
		try {
			modeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	public void onAutoBattle() {
		gameMode = GameMode.AUTO;
		setupNormalMode();
		switchToBattleWindow();
	}

	@Override
	public void onControlledBattle() {
		gameMode = GameMode.CONTROLLED;
		setupNormalMode();
		switchToBattleWindow();
	}

	@Override
	public void onTowerMode() {
		gameMode = GameMode.TOWER;
		setupTowerMode();
		handleTower();
	}

	@Override
	public void onReturnToCreateTeamWindow() {
		try {
			teamController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private BattleController getCurrentBattleController() {
		if (gameMode == GameMode.TOWER && towerModeTowerManager != null) {
			return towerModeTowerManager.getCurrentBattleController();
		}
		return normalModeBattleController;
	}

	private int getTowerFloorNumber() {
		return towerModeTowerManager != null ? towerModeTowerManager.getFloorNumber() : 0;
	}

	private int getCurrentRoomIndex() {
		return towerModeTowerManager != null ? towerModeTowerManager.getCurrentRoomIndex() : 0;
	}

	@Override
	public BattleState onAutoTurn() {
		StrategyRandom strategyRandom = new StrategyRandom(normalModeBattleController);
		return strategyRandom.playAutoTurn();
	}

	@Override
	public BattleState onUseItem(Item item) {
		BattleController battleController = battleControllerForManualTurn();
		if (battleController != null && item != null) {
			battleController.useAction(new UseItem(item));
			return battleController.getState();
		}
		return null;
	}

	@Override
	public BattleState onSwapBugemon(Bugemon bugemon) {
		BattleController battleController = battleControllerForManualTurn();
		if (battleController != null && bugemon != null) {
			battleController.useAction(new Swap(bugemon));
			return battleController.getState();
		}
		return null;
	}

	@Override
	public BattleState onUseAbility(Ability ability) {
		BattleController battleController = battleControllerForManualTurn();
		if (battleController != null && ability != null) {
			battleController.useAction(new UseAbility(ability));
			return battleController.getState();
		}
		return null;
	}

	@Override
	public void onBattleStateChecked(BattleState state, ActionEvent event) {
		handleBattleEndCheckMessage(new BattleEndCheckMessage(state, event));
	}

	@Override
	public void onTowerFlee() {
		handleTowerFlee();
		switchToNextRoomWindow();
	}

	@Override
	public void onReturnToMode() {
		try {
			modeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onContinue() {
		handleTower();
	}

	@Override
	public void onRewardChosen(Reward reward, ActionEvent event){
		handleLevelUpRewardChoice(reward, event);
	}

	@Override
	public Bugemon getLevelUpBugemon() {
		return pendingLevelUpBugemons.peekFirst();
	}

	@Override
	public List<Reward> getLevelUpRewards() {
		Bugemon current = pendingLevelUpBugemons.peekFirst();
		if (current != null && pendingRewardBattleController != null) {
			return pendingRewardBattleController.getRewards(current);
		}
		return List.of();
	}

	private void switchToBattleWindow() {
		BattleController battleController = getCurrentBattleController();
		int towerFloorNumber = gameMode == GameMode.TOWER ? getTowerFloorNumber() : 0;
		int currentRoomIndex = gameMode == GameMode.TOWER ? getCurrentRoomIndex() : 0;
		battleWindowController = new BattleWindowController(
				stage,
				this,
				player,
				battleController,
				gameMode,
				towerFloorNumber,
				currentRoomIndex
		);
		try {
			battleWindowController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// /**
	//  * initialises a new floorRewardController and switches to the window
	//  */
	// private void switchToFloorRewardWindow() {
	// 	floorRewardController = new FloorRewardController(stage, this, getTowerFloorNumber(), getCurrentRoomIndex());
	// 	try {
	// 		floorRewardController.show();
	// 	} catch (Exception e) {
	// 		e.printStackTrace();
	// 	}
	// }

	@Override
	public void onReturnFloorRewardWindow() {
		if (floorRewardController == null) {
			floorRewardController = new FloorRewardController(stage, this, this);
		}
		try {
			floorRewardController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onChooseBugemonReward(FloorRewardController.RewardChoice rewardChoice) {
		pendingFloorRewardChoice = rewardChoice;
		if (chooseBugemonController == null) {
			chooseBugemonController = new ChooseBugemonController(stage, floorRewardController, player);
		}
		try {
			chooseBugemonController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onBugemonChosen(Bugemon bugemon) {
		if (pendingFloorRewardChoice == FloorRewardController.RewardChoice.STAT) {
			Reward reward = new Reward(bugemon);
			reward.configureReward(RewardType.COMBINATION);
			bugemon.changeBaseStats(reward.getStats());
			bugemon.changeFightStats(reward.getStats());
			switchToNextRoomWindow();
			return;
		}

		Ability newAbility = this.getAbilityService().getRandomAbility(bugemon.getType(), bugemon.getAbilities());
		if (newAbility == null) {
			switchToNextRoomWindow();
			return;
		}

		if (attackReplacementController == null) {
			attackReplacementController = new AttackReplacementController(stage, this);
		}
		try {
			attackReplacementController.show(bugemon, newAbility);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onAttackReplaced(Bugemon bugemon, Ability newAbility, Ability oldAbility) {
		bugemon.swapAbility(newAbility, oldAbility);
		switchToNextRoomWindow();
	}

	@Override
	public void onReturnToChooseBugemon() {
		if (chooseBugemonController == null) {
			return;
		}
		try {
			chooseBugemonController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onObjectReward(Item rewardItem) {
		player.getInventory().addItem(rewardItem, 1);
		switchToNextRoomWindow();
	}

	private void switchToNextRoomWindow() {
		if (nextRoomController == null) {
			nextRoomController = new NextRoomController(stage, this);
		}

		try {
			nextRoomController.show(hasFledBattle());
			resetFledBattle();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRegister() {
		if (modeController == null) {
			modeController = new ModeController(stage, this, player, this.getBugemonService());
		}
		modeController.show();
	}
}
