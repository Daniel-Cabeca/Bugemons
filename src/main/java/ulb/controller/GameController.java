package ulb.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ulb.controller.strategy.StrategyRandom;
import ulb.controller.towerManager.TowerManager;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
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

	public void setPlayer(Player player) {
		this.player = player;

	}

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
		System.out.println(player);
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
	 */
	public void switchToBattleWindow(Team teamA, boolean automatic, ActionEvent event) {
		// create battle with the selected bugemons for both players
		Team teamB = new Team();
		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(teamA);
		} catch (Exception e) {
		}

		// without multiplayer, player is always teamA

		try {
			// NewBattleWindow.fxml for graphic interface (connection methods to view needed, placeholders for now)
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/NewBattleWindow.fxml"));
			Parent battleWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleWindow);

			BattleWindow controller = loader.getController();
			controller.setGameController(this);
			controller.setBattleController(normalModeBattleController);

			controller.setPlayer(player);
			controller.initializeBattle(teamA, teamB, player.getInventory(), automatic);

		} catch (IOException e) {
			System.err.println("Failed to load battle window: " + e.getMessage());
		}
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

}
