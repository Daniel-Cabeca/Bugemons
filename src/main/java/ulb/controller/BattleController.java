package ulb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleSnapshot;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.view.BattleWindow;
import ulb.view.BattleMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import ulb.model.Player;
import ulb.model.item.Item;
import ulb.model.team.OpponentTeamGenerator;

public class BattleController {
	private Player player;
	private BattleSnapshot battleSnapshot;

	public BattleController(Player player) {
		this.player = player;
	}

	public void switchToBattleMenu(List<String> selectedBugemons, ActionEvent event) {
		List<Bugemon> teamABugemons = new ArrayList<Bugemon>();
		for (String bugemon: selectedBugemons) {
			teamABugemons.add(new Bugemon(bugemon.toLowerCase()));
		}
		Team teamA = new Team(teamABugemons);
		player.setTeam(teamA);

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
	 * @param teamA the list of selected bugemon names to create the teams for the battle
	 * @throws IOException if the battle window FXML file cannot be loaded
	 */
	public void switchToBattleWindow(Team teamA, boolean automatic, ActionEvent event) {
		// create battle with the selected bugemons for both players
		Team teamB = new Team();
		try {
			teamB = OpponentTeamGenerator.generateRandomOpponentTeam(teamA);
		}
		catch (Exception e) {}

		// without multiplayer, player is always teamA
		battleSnapshot = new BattleSnapshot(new Battle(teamA, teamB), true);

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
	 * Uses an item from the player's inventory during battle and updates the inventory display and the bugemon's stats
	 * @param item the item to be used from the player's inventory
	 */
	public void useItem(Item item) {
		player.getInventory().removeItem(item);
		Bugemon activeBugemon = battleSnapshot.getActiveBugemonSelf();
		Bugemon opponentBugemon = battleSnapshot.getActiveBugemonOpponent();

		if (item.getEffect().getTarget().equals("adversaire")) {
			item.use(opponentBugemon);
		} else {
			item.use(activeBugemon);
		}

		if (item.getEffect().getType().equals("switch")) {
			// TODO: implement ability to choose next bugemon, for now: switch to first available
			Team playerTeam = battleSnapshot.getTeamSelf();
			Bugemon nextBugemon = playerTeam.getMembers().stream()
					.filter(b -> !b.isKO() && b != activeBugemon)
					.findFirst()
					.orElse(null);
			if (nextBugemon != null) {
				battleSnapshot.setActiveBugemonSelf(nextBugemon);
			}
		}
	}

	public Bugemon getActiveBugemonSelf(){ return battleSnapshot.getActiveBugemonSelf();}
	public Bugemon getActiveBugemonOpponent(){ return battleSnapshot.getActiveBugemonOpponent();}

	public void setActiveBugemon(Bugemon bugemon){ battleSnapshot.setActiveBugemonSelf(bugemon);}

	public Player getPlayer(){

        return this.player;
    }

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void useAbility(Ability ability){
		battleSnapshot.useAbility(ability);
	}


}
