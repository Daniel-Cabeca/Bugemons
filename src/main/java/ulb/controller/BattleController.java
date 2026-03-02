package ulb.controller;

import java.io.IOException;
import java.util.List;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleSnapshot;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonDatabase;
import ulb.model.type.Type;
import ulb.view.BattleWindow;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import ulb.model.Player;
import ulb.model.item.Item;

public class BattleController {
	private Player player;
	private BattleSnapshot battleSnapshot;

	public BattleController(Player player) {
		this.player = player;
	}

	/**
	 * Switches to the battle window with the selected bugemons
	 * @param selectedBugemons the list of selected bugemon names to create the teams for the battle
	 * @throws IOException if the battle window FXML file cannot be loaded
	 */
	public void switchToBattleWindow(List<String> selectedBugemons, ActionEvent event) {
		// create battle with the selected bugemons for both players
		// (for now, we will use the same team for both players)

		// create placeholder bugemons based on the names
		Bugemon bugemon1 = BugemonDatabase.getInstance().get("florachu").spawn();
		Bugemon bugemon2 = BugemonDatabase.getInstance().get("bugzilla").spawn();
		Bugemon bugemon3 = BugemonDatabase.getInstance().get("pyricore").spawn();
		Bugemon bugemon4 = BugemonDatabase.getInstance().get("pyroxis").spawn();
		Bugemon bugemon5 = BugemonDatabase.getInstance().get("mergeau").spawn();
		Bugemon bugemon6 = BugemonDatabase.getInstance().get("buildwave").spawn();

		Team team1 = new Team(List.of(bugemon1, bugemon2, bugemon3, bugemon4, bugemon5, bugemon6));

		Bugemon bugemon1_copy = BugemonDatabase.getInstance().get("granitron").spawn();
		Bugemon bugemon2_copy = BugemonDatabase.getInstance().get("pebblit").spawn();
		Bugemon bugemon3_copy = BugemonDatabase.getInstance().get("refaquix").spawn();
		Bugemon bugemon4_copy = BugemonDatabase.getInstance().get("crasheon").spawn();
		Bugemon bugemon5_copy = BugemonDatabase.getInstance().get("exceflam").spawn();
		Bugemon bugemon6_copy = BugemonDatabase.getInstance().get("verdurion").spawn();

		Team team2 = new Team(List.of(bugemon1_copy, bugemon2_copy, bugemon3_copy, bugemon4_copy, bugemon5_copy, bugemon6_copy));

		// without multiplayer, player is always teamA
		battleSnapshot = new BattleSnapshot(new Battle(team1, team2), true);

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/BattleWindow.fxml"));
			Parent battleWindow = loader.load();

			Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    	stage.getScene().setRoot(battleWindow);

			BattleWindow controller = loader.getController();
			controller.setBattleController(this);
			controller.initializeBattle(team1, team2, player.getInventory());
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



	public void Damage(Ability ability){
		battleSnapshot.useAbility(ability);
	}


}
