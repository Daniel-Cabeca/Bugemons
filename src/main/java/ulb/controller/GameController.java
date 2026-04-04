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
import ulb.controller.action.TeamController;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.controller.strategy.StrategyRandom;
import ulb.controller.towerManager.FloorManager;
import ulb.controller.towerManager.RoomManager;
import ulb.controller.towerManager.TowerManager;
import ulb.model.battle.BattleState;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Item;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.model.reward.Reward;
import ulb.view.WindowPath;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


public class GameController extends Application {
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
		setPlayer(new Player("Player"));
		this.stage = primaryStage;

		Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");

		switchWindow(WindowPath.MODE);

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

			if (loader.getController() instanceof ulb.view.windows.Window window) {
				window.setGameController(this);
				window.onLoad();
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

	public GameController() {}

	public void setPlayer(Player player) {this.player = player;}

	public Player getPlayer() {return this.player;}

	public Team getTeam() {return this.player.getTeam();}


	/**
	 * Setups the right settings for the normal mode
	 */
	public void setupNormalMode(){
		Team playerTeam = player.getTeam();
		Team opponentTeam = new Team();
		try{
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);
		}catch(Exception e){
			System.err.println(e);
		}
		Battle battle = new Battle(playerTeam, opponentTeam, player);
		this.normalModeBattleController = new BattleController(player, battle, ParticipantLabel.TEAM_A);
		StrategyRandom strategyRandom = new StrategyRandom(battle);
		Thread thread = new Thread(strategyRandom);
		thread.setDaemon(true);
		thread.start();
	}

	/**
	 * Setups the right settings for the tower mode
	 */
	public void setupTowerMode(){
		towerModeTowerManager = new TowerManager(this.getPlayer());
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
		switchWindow(WindowPath.LEVEL_UP);
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
			switchWindow(WindowPath.NEXT_ROOM);
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
		switchWindow(WindowPath.BATTLE_END);
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
	public void handleTower(ActionEvent event)  {

		towerModeTowerManager.getCurrentFloorManager().nextRoom();
		towerModeTowerManager.nextFloor();

		if (!towerModeTowerManager.isTowerCompleted()) {
			FloorManager floorManager = towerModeTowerManager.getCurrentFloorManager();

			RoomManager roomManager = floorManager.getCurrentRoomManager();
			Room currentRoom = roomManager.getRoom();
			RoomType type = currentRoom.getRoomType();

			switch (type) {
				case BATTLE, BOSS:
					switchWindow(WindowPath.BATTLE);
					break;

				case REWARD:
					switchWindow(WindowPath.FLOOR_REWARD);
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

	public void setupTeam(List<String> selectedBugemons){
		TeamController teamController = new TeamController(this.player);
		teamController.setTeam(selectedBugemons);
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
		Message response = null; // not all received messages need a response
		MessageType messageType = m.getMessageType();
		switch (messageType) {
			case SWITCH_WINDOW:
				switchWindow(((SwitchWindowMessage) m).getSwitchWindow());
				break;
			case SETUP_TEAM:
				setupTeam(((SetupTeamMessage) m).getSelectedBugemons());
				break;
			case SETUP_GAME_MODE:
				handleSetupGameModeMessage((SetupGameModeMessage) m);
				break;
			case TOWER_FLEE:
				handleTowerFlee();
				switchWindow(WindowPath.NEXT_ROOM);
				break;
			case TOWER_NEXT_ROOM:
				handleTower(((TowerNextRoomMessage) m).getEvent());
				break;
			case GET_INFO:
				response = handleGetInfoMessage((GetInfoMessage) m);
				break;
			case AUTO_TURN_REQUEST:
				StrategyRandom strategyRandom = new StrategyRandom(normalModeBattleController);
				BattleState state = strategyRandom.playAutoTurn();
				response = new AutoTurnResponseMessage(state);
				break;
			case USE_ITEM_REQUEST:
				response = handleUseItemMessage((UseItemRequestMessage) m);
				break;
			case SWAP_REQUEST:
				response = handleSwapMessage((SwapRequestMessage) m);
				break;
			case USE_ABILITY_REQUEST:
				response = handleUseAbilityMessage((UseAbilityRequestMessage) m);
				break;
			case BATTLE_END_CHECK:
				handleBattleEndCheckMessage((BattleEndCheckMessage) m);
				break;
			case LEVEL_UP:
				LevelUpMessage levelUpMessage = (LevelUpMessage) m;
				handleLevelUpRewardChoice(levelUpMessage.getReward(), levelUpMessage.getEvent());
		}
        return response;
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
			boolean isTower = gameMode == GameMode.TOWER;
			if (!startLevelUpSequenceIfNeeded(battleController, isTower, event)) {
				if (!isTower) {
					int xp = normalModeBattleController != null ? normalModeBattleController.getTotalXP() : 0;
					handleBattleEnd(true, xp);
				} else {
					switchWindow(WindowPath.NEXT_ROOM);
				}
			}
		} else if (state == BattleState.LOST) {
			int xp = normalModeBattleController != null ? normalModeBattleController.getTotalXP() : 0;
			handleBattleEnd(false, xp);
		}
	}
}
