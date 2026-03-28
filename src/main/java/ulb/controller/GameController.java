package ulb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ulb.communication.Message;
import ulb.communication.types.*;
import ulb.controller.action.TeamController;
import ulb.controller.strategy.StrategyRandom;
import ulb.controller.towerManager.FloorManager;
import ulb.controller.towerManager.RoomManager;
import ulb.controller.towerManager.TowerManager;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.model.reward.Reward;
import ulb.view.ViewManager;
import ulb.view.windows.BattleEndWindow;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.FloorRewardWindow;
import ulb.view.windows.LevelUpWindow;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;


public class GameController {
	private Player player;
	private Team opponentTeam;
	private TowerManager towerModeTowerManager;
	private BattleController normalModeBattleController;
	private ViewManager viewManager;

	private boolean isNextFloor;
	private GameMode gameMode;
	private final Deque<Bugemon> pendingLevelUpBugemons = new ArrayDeque<>();
	private BattleController pendingRewardBattleController;
	private boolean rewardSequenceReturnsToNextRoom;
	private int pendingBattleXP;

	public GameController() {}

	public GameController(TowerManager towerManager){
		this.towerModeTowerManager = towerManager;
	}

	public void setPlayer(Player player) {this.player = player;}

	public Player getPlayer() {return this.player;}

	public Team getTeam() {return this.player.getTeam();}


	/**
	 * Setups the right settings for the normal mode
	 */
	public void setupNormalMode(){
		Team playerTeam = player.getTeam();
		try{
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);
		}catch(Exception e){
			System.err.println(e);
		}
		Battle battle = new Battle(playerTeam, opponentTeam, player);
		this.normalModeBattleController = new BattleController(player, battle, ParticipantLabel.TEAM_A);
		StrategyRandom strategyRandom = new StrategyRandom(battle);
		Thread thread = new Thread(strategyRandom);
		thread.start();
	}

	/**
	 * Setups the right settings for the tower mode
	 */
	public void setupTowerMode(){
		towerModeTowerManager = new TowerManager(this.getPlayer());
	}

	/**
	 * Switches to the battle window in tower mode with the selected bugemons
	 *
	 * @param teamA the player's team of bugemons
	 */
	public void switchToTowerBattleWindow(Team teamA, ActionEvent event) { //TODO refactor

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/BattleWindow.fxml"));
			Parent battleWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleWindow);

			BattleWindow controller = loader.getController();
			controller.setViewManager(viewManager);
			controller.setTowerManager(towerModeTowerManager);

			controller.setBattleController(towerModeTowerManager.getCurrentRoomManager().getRoomBattleController());
			// always manual battle in tower mode
			controller.initializeBattle(teamA, player.getInventory(), GameMode.TOWER);

		} catch (IOException e) {
			System.err.println("Failed to load battle window: " + e.getMessage());
		}
	}

	/**
	 * Switches to the floor reward window (rewards not functional yet)
	 */
	public void switchToFloorRewardWindow(ActionEvent event) { //TODO refactor
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/FloorRewardWindow.fxml"));
			Parent floorRewardWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(floorRewardWindow);

			FloorRewardWindow controller = loader.getController();
			controller.setViewManager(viewManager);
			controller.setTowerManager(towerModeTowerManager);
			controller.initializeLabels();

		} catch (IOException e) {
			System.err.println("Failed to load floor reward window: " + e.getMessage());
		}
	}

	/**
	 * Switches from the current battle view to the battle end window and displays the result.
	 *
	 * @param victory {@code true} if the player won the battle, {@code false} if the player lost
	 */
	public void switchToBattleEndWindow(boolean victory, ActionEvent event) { //TODO refactor
		int totalXP = 0;
		if (normalModeBattleController != null) {
			totalXP = normalModeBattleController.getTotalXP();
		}
		this.switchToBattleEndWindow(victory, totalXP, event);
	}

	public void switchToBattleEndWindow(boolean victory, int totalXP, ActionEvent event) { //TODO refactor
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/BattleEndWindow.fxml"));
			Parent battleEndWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleEndWindow);

			BattleEndWindow controller = loader.getController();
			controller.setViewManager(viewManager);
			controller.setResult(victory, totalXP);

		} catch (IOException e) {
			System.err.println("Failed to load battle_end_window: " + e.getMessage());
		}
	}

	/**
	 * Switches to a button to go to the next room when in tower mode
	 *
	 * @param event the action triggered by clicking the confirm team button
	 */
	public void switchToNextRoomWindow(ActionEvent event) { //TODO refactor
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/NextRoomWindow.fxml"));
			Parent nextRoomWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(nextRoomWindow);

			ulb.view.windows.NextRoomWindow controller = loader.getController();
			controller.setViewManager(viewManager);
			// updates the button to say "Prochain étage" (instead of "Prochaine salle") if the floor is completed
			controller.updateButtonText(isNextFloor);
		} catch (IOException e) {
			System.err.println("Failed to load next_room_window: " + e.getMessage());
		}
	}

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
		switchToLevelUpWindow(event);
		return true;
	}

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
			switchToLevelUpWindow(event);
			return;
		}

		boolean returnToNextRoom = rewardSequenceReturnsToNextRoom;
		int totalXP = pendingBattleXP;
		clearPendingLevelUpState();
		if (returnToNextRoom) {
			switchToNextRoomWindow(event);
		} else {
			switchToBattleEndWindow(true, totalXP, event);
		}
	}

	private void switchToLevelUpWindow(ActionEvent event) { //TODO refactor
		Bugemon currentBugemon = pendingLevelUpBugemons.peekFirst();
		if (currentBugemon == null || pendingRewardBattleController == null) {
			return;
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/LevelUpWindow.fxml"));
			Parent levelUpWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(levelUpWindow);

			LevelUpWindow controller = loader.getController();
			controller.setViewManager(viewManager);
			controller.initializeRewardSelection(currentBugemon, pendingRewardBattleController.getRewards(currentBugemon));
		} catch (IOException e) {
			System.err.println("Failed to load level_up_window: " + e.getMessage());
		}
	}

	private void clearPendingLevelUpState() {
		pendingLevelUpBugemons.clear();
		pendingRewardBattleController = null;
		rewardSequenceReturnsToNextRoom = false;
		pendingBattleXP = 0;
	}

	/**
	 * Handles each room when in tower mode: switches to the right window and initializes its content
	 *
	 * @param teamA the team that the player chose for the tower mode
	 */
	public void handleTower(Team teamA, ActionEvent event)  {

		if (!towerModeTowerManager.isTowerCompleted()) {
			isNextFloor = false;
			FloorManager floorManager = towerModeTowerManager.getCurrentFloorManager();

			if (!floorManager.isFloorCompleted()) {
				RoomManager roomManager = floorManager.getCurrentRoomManager();
				Room currentRoom = roomManager.getRoom();
				RoomType type = currentRoom.getRoomType();

				switch (type) {
					case BATTLE:
						switchToTowerBattleWindow(teamA,event);
						roomManager.initializeRoomContent(type);
						roomManager.setRoomCompleted(true);
						break;

					case BOSS:
						switchToTowerBattleWindow(teamA,event);
						roomManager.initializeRoomContent(type);
						roomManager.setRoomCompleted(true);
						isNextFloor = true;
						break;

					case REWARD:
						switchToFloorRewardWindow(event);
						roomManager.setRoomCompleted(true);
						break;

					default:
						roomManager.setRoomCompleted(true);
						break;
				}
				floorManager.nextRoom();
			}
			towerModeTowerManager.nextFloor();
		}
	}

	public void setupTeam(List<String> selectedBugemons){
		TeamController teamController = new TeamController(this.player);
		teamController.setTeam(selectedBugemons);
	}

	public TowerManager getTowerManager() {
		return towerModeTowerManager;
	}

	public void setViewManager(ViewManager vManager) {
		this.viewManager = vManager;
	}

	/**
	 * Handles a message received from ViewManager
	 * @param m the message received from ViewManager
	 * @return m the answer message to transfer to ViewManager
	 */
	public Message handleMessage(Message m) {
		if (m instanceof SwitchWindowMessage) { // pour l'instant, on n'a pas de vérification à faire pour ce type d'action,
			// mais c'est ici qu'on doit faire les vérif
			return m;
		} else if (m instanceof SetupTeamMessage) {
			setupTeam(((SetupTeamMessage) m).getSelectedBugemons());
		} else if (m instanceof SetupGameModeMessage) {
			handleSetupGameModeMessage((SetupGameModeMessage) m);
		} else if (m instanceof GetInfoMessage) {
			if (gameMode == GameMode.TOWER) {
				m = new SetupGameModeMessage(gameMode, getTeam(), opponentTeam, player.getInventory(), towerModeTowerManager.getCurrentBattleController());
			} else {
				m = new SetupGameModeMessage(gameMode, getTeam(), opponentTeam, player.getInventory(), normalModeBattleController);
			}
		}
        return m;
    }

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

}
