package ulb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleState;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.model.type.Effectiveness;
import ulb.model.type.Type;
import ulb.view.handler.WindowContainer;
import ulb.view.windows.BattleEndWindow;
import ulb.view.windows.BattleMenu;
import ulb.view.windows.BattleWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ulb.controller.action.*;
import ulb.model.Player;
import ulb.model.item.Item;
import ulb.model.team.OpponentTeamGenerator;

public class BattleController {
	private Player player;
	private WindowContainer windowContainer;
	private Battle battle;
	private boolean isTeamA;
	private int floorNumber = 1;
	private boolean isBossFight = false;

	public BattleController(){
		this.windowContainer = new WindowContainer();
	}

	public BattleController(Player player, Battle battle, boolean isTeamA) {
		this.player = player;
		this.battle = battle;
		this.isTeamA = isTeamA;
	}

	/**
	 * Switches to the battle type menu
	 *
	 * @param event            the action triggered by clicking the confirm team button
	 */
	public void switchToBattleMenu(ActionEvent event) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/BattleMenu.fxml"));
			Parent battleMenu = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleMenu);

			BattleMenu controller = loader.getController();
			controller.setBattleController(this);
			controller.displayTeam();

		} catch (IOException e) {
			System.err.println("Failed to load battle menu window: " + e.getMessage());
		}
	}

	/**
	 * Switches to the battle window with the selected bugemons
	 *
	 * @param teamA the player's team of bugemons
	 * @throws IOException if the battle window FXML file cannot be loaded
	 */
	public void switchToBattleWindow(Team teamA, boolean automatic, ActionEvent event) {
		// create battle with the selected bugemons for both players
		Team teamB = new Team();
		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(teamA);
		} catch (Exception e) {
		}

		// without multiplayer, player is always teamA
		// battleSnapshot = new BattleSnapshot(new Battle(teamA, teamB, this.player, playerB), true);

		try {
			// NewBattleWindow.fxml for graphic interface (connection methods to view needed, placeholders for now)
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/NewBattleWindow.fxml"));
			Parent battleWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.getScene().setRoot(battleWindow);

			BattleWindow controller = loader.getController();
			controller.setBattleController(this);
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
			controller.setResult(victory, getTotalXP());

		} catch (IOException e) {
			System.err.println("Failed to load battle_end_window: " + e.getMessage());
		}
	}

	/**
	 * Checks if an item can be used or not given the stats of the bugemon
	 *
	 * @param item the item that needs to be checked
	 * @return if the item can be used or not (boolean)
	 */
	public boolean checkItem(Item item) {
		return this.battle.checkItem(item, isTeamA);
	}


	/**
	 * Gives the Bugemon that the snapshot views as its active Bugemon.
	 *
	 * @return The snapshot's active Bugemon
	 */
	public Bugemon getActiveBugemonSelf() {
		if (this.isTeamA) {
			return battle.getActiveBugemonA();
		}
		else {
			return battle.getActiveBugemonB();
		}
	}

	public Bugemon getActiveBugemonOpponent() {
		if (this.isTeamA){
			return this.battle.getActiveBugemonB();
		}
		return this.battle.getActiveBugemonA();
	}

	public Player getPlayer() {
		return this.player;
	}

	public Team getTeam(){
		if (isTeamA){
			return this.battle.getTeamA();
		}
		return this.battle.getTeamB();

	}

	public boolean isBugemonAKO() {
		return this.battle.isBugemonAKO();
	}

	public boolean isBugemonBKO() {
		return this.battle.isBugemonBKO();
	}

	public boolean isTeamAKO() {
		return this.battle.isTeamAKO();
	}

	public boolean isTeamBKO() {
		return this.battle.isTeamBKO();
	}

	public void useAction(Action action) {
		this.battle.setAction(action, isTeamA);
	}

	public Vector<Action> getAvailableAction(){
		return this.battle.getAvailableActions(isTeamA);
	}

	public Vector<Bugemon> getAvailableBugemons(){
		return this.battle.getAvailableBugemons(isTeamA);
	}

	public boolean isGameFinished(){
		return this.battle.isGameFinished();
	}

	public BattleState getState(){
		return this.battle.getState(isTeamA);
	}

	/**
	 * Gets the effectiveness factor of the current ability
	 *
	 * @param ability the ability whose type effectiveness is evaluated
	 * @return the effectiveness message (or null if the effectiveness is normal)
	 */
	public String getEffectiveness(Ability ability) {
		return battle.getEffectiveness(ability, getActiveBugemonOpponent());
	}

	public List<String> getLogMsg() {
		return this.battle.getLogMsg();
	}

	public void clearLogMsg() {
		this.battle.clearLogMsg();
	}

	private int getTotalXP() { return this.battle.computeTotalXP(this.battle.getTeamB());}
}
