package ulb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ulb.controller.strategy.StrategyRandom;
import ulb.controller.towerManager.FloorManager;
import ulb.controller.towerManager.RoomManager;
import ulb.controller.towerManager.TowerManager;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.view.windows.BattleEndWindow;
import ulb.view.windows.BattleMenu;
import ulb.view.windows.BattleWindow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameController {
	private Player player;
	private TowerManager towerModeTowerManager;
	private BattleController normalModeBattleController;


	public GameController(){
	}

	public GameController(TowerManager towerManager){
		this.towerModeTowerManager = towerManager;
	}

	public void setPlayer(Player player) {this.player = player;}

	public Player getPlayer() {return this.player;}

	public Team getTeam() {return this.player.getTeam();}

	/**
	 * Adds the chosen Bugemons to the player's team
	 *
	 * @param player The player whose team needs to be set up
	 * @param selectedBugemons The bugemons the player selected to be in their team
	 */
	public void setupPlayer(Player player , List<String> selectedBugemons){
		this.setPlayer(player);

		List<Bugemon> teamABugemons = new ArrayList<Bugemon>();
		for (String bugemon : selectedBugemons) {
			teamABugemons.add(new Bugemon(bugemon.toLowerCase()));
		}

		Team playerTeam = new Team(teamABugemons);
		player.setTeam(playerTeam);
	}

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
		this.normalModeBattleController = new BattleController(player, battle, true);
		StrategyRandom strategyRandom = new StrategyRandom(battle);
		Thread thread = new Thread(strategyRandom);
		thread.start();
	}

	/**
	 * Setups the right settings for the tower mode
	 */
	public void setupTowerMode(){
		TowerManager towerModeTowerManager = new TowerManager(this.getPlayer());
	}

	/**
	 * Switches to the battle type menu
	 *
	 * @param event the action triggered by clicking the confirm team button
	 */
	public void switchToBattleMenu(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/BattleMenu.fxml"));
			Parent battleMenu = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleMenu);

			BattleMenu controller = loader.getController();
			controller.setGameController(this);
			controller.displayTeam();

		} catch (IOException e) {
			System.err.println("Failed to load battle menu window: " + e.getMessage());
		}
	}

	/**
	 * Switches to the battle window with the selected bugemons
	 *
	 * @param teamA the player's team of bugemons
	 * @param automatic whether the battle is in automatic mode or not
	 * @param event the action triggered by clicking the confirm team button
	 */
	public void switchToBattleWindow(Team teamA, boolean automatic, ActionEvent event) {
		// generate random opponent team
		Team teamB = new Team();
		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(teamA);
		} catch (Exception e) {
		}

		// without multiplayer, player is always teamA

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/NewBattleWindow.fxml"));
			Parent battleWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleWindow);

			BattleWindow controller = loader.getController();
			controller.setGameController(this);
			controller.setBattleController(normalModeBattleController);

			controller.setPlayer(player);
			controller.initializeBattle(teamA, teamB, player.getInventory(), automatic, false);

		} catch (IOException e) {
			System.err.println("Failed to load battle window: " + e.getMessage());
		}
	}

	/**
	 * Switches to the battle window in tower mode with the selected bugemons
	 *
	 * @param teamA the player's team of bugemons
	 * @param event the action triggered by clicking the confirm team button
	 */
	public void switchToTowerBattleWindow(Team teamA, ActionEvent event) {
		// generate random opponent team
		Team teamB = new Team();
		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(teamA);
		} catch (Exception e) {
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/NewBattleWindow.fxml"));
			Parent battleWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleWindow);

			BattleWindow controller = loader.getController();
			controller.setGameController(this);
			controller.setTowerManager(towerModeTowerManager);

			controller.setBattleController(towerModeTowerManager.getCurrentRoomManager().getRoomBattleController());
			controller.setPlayer(player);
			// always manual battle in tower mode
			controller.initializeBattle(teamA, teamB, player.getInventory(), false, true);

		} catch (IOException e) {
			System.err.println("Failed to load battle window: " + e.getMessage());
		}
	}

	public void switchToTowerRewardWindow(Team teamA, ActionEvent event) {
		// TODO
	}

	/**
	 * Switches from the current battle view to the battle end window and displays the result.
	 *
	 * @param victory {@code true} if the player won the battle, {@code false} if the player lost
	 */
	public void switchToBattleEndWindow(boolean victory, ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/BattleEndWindow.fxml"));
			Parent battleEndWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleEndWindow);

			BattleEndWindow controller = loader.getController();
			controller.setPlayer(player);
			controller.setResult(victory);

		} catch (IOException e) {
			System.err.println("Failed to load battle_end_window: " + e.getMessage());
		}
	}

	/**
	 * Switches to a button to go to the next room when in tower mode
	 *
	 * @param event the action triggered by clicking the confirm team button
	 */
	public void switchToNextRoomWindow(ActionEvent event) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/NextRoomWindow.fxml"));
			Parent nextRoomWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(nextRoomWindow);

			ulb.view.windows.NextRoomWindow controller = loader.getController();
			controller.setGameController(this);

		} catch (IOException e) {
			System.err.println("Failed to load next_room_window: " + e.getMessage());
		}
	}

	/**
	 * Handles each room when in tower mode: switches to the right window and initializes its content
	 *
	 * @param teamA the team that the player chose for the tower mode
	 * @param event the action triggered by clicking the confirm team button
	 */
	public void handleTower(Team teamA, ActionEvent event)  {
		if (towerModeTowerManager == null) {
			towerModeTowerManager = new TowerManager(player);
		}

		if (!towerModeTowerManager.isTowerCompleted()) {
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
						break;

					case REWARD:
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

}
