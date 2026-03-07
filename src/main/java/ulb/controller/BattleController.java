package ulb.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleSnapshot;
import ulb.model.battle.BattleState;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.view.BattleEndWindow;
import ulb.view.BattleWindow;
import ulb.view.BattleMenu;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import ulb.controller.action.UseItem;
import ulb.model.Player;
import ulb.model.item.Item;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.Effect;

public class BattleController {
	private Player player;
	private BattleSnapshot battleSnapshot;

	public BattleController(Player player) {
		this.player = player;
	}

	/**
	 * Switches to the battle type menu
	 *
	 * @param selectedBugemons the list of selected bugemons in create team menu
	 * @param event the action triggered by clicking the confirm team button
	 */
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
	 * @param teamA the player's team of bugemons
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
	 * Checks if an item can be used or not given the stats of the bugemon
	 * @param item the item that needs to be checked
	 * @return if the item can be used or not (boolean)
	 */
	public boolean checkItem(Item item) {
		if (item.getEffect().getType().equals(Effect.EffectType.SOIN)) {
			if (battleSnapshot.getTeamView()) {
				int baseHp = battleSnapshot.getBattle().getActiveBugemonA().getBaseStats().getHp();
				int fightHP = battleSnapshot.getBattle().getActiveBugemonA().getHp();
				if (baseHp == fightHP) {
					return false;
				}
			}
			else {
				int baseHp = battleSnapshot.getBattle().getActiveBugemonB().getBaseStats().getHp();
				int fightHP = battleSnapshot.getBattle().getActiveBugemonB().getHp();
				if (baseHp == fightHP) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Uses an item from the player's inventory during battle and updates the inventory display and the bugemon's stats
	 * @param item the item to be used from the player's inventory
	 */
	public void useItem(Item item) {
		battleSnapshot.useAction(new UseItem(item));
		player.getInventory().removeItem(item);
	}

	public Bugemon getActiveBugemonSelf(){ return battleSnapshot.getActiveBugemonSelf();}

	public Bugemon getActiveBugemonOpponent(){ return battleSnapshot.getActiveBugemonOpponent();}

	public void setActiveBugemon(Bugemon bugemon){ battleSnapshot.setActiveBugemonSelf(bugemon);}

	public Player getPlayer(){ return this.player;}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void useAbility(Ability ability){
		battleSnapshot.useAbility(ability);
	}

	/**
	 * Uses a random ability for the current active Bugemon of the specified team.
	 *
	 * @param isTeamA {@code true} to make Team A (the player) use a random ability,
	 *                {@code false} to make Team B (the opponent) use a random ability
	 */
	public void useRandomAbility(boolean isTeamA) {

		Ability ability;
		if (isTeamA) {
			ability = battleSnapshot.getRandomAbilitySelf();
			useAbility(ability);
		} else {
			ability = battleSnapshot.getRandomAbilityOpponent();
			battleSnapshot.useAbilityOnA(ability);
		}
	}

	/**
	 * Plays a turn in the automatic battle mode
	 *
	 * @return BattleState indicating if the player lost, won or if the battle is ongoing
	 */
	public BattleState playAutoTurn() {
		// the player's turn (always plays first)
		useRandomAbility(true);
		if (isTeamBKO()) {
			return BattleState.WON;
		}
		if (isBugemonBKO()) {
			switchBugemonB();
		}

		// the opponent's turn
		useRandomAbility(false);
		if (isTeamAKO()) {
			return BattleState.LOST;
		}
		if (isBugemonAKO()) {
			switchBugemonA();
		}

		return BattleState.INGAME;
	}

	public boolean isBugemonAKO(){return battleSnapshot.getBattle().isBugemonAKO();}
	public boolean isBugemonBKO(){return battleSnapshot.getBattle().isBugemonBKO();}

	public boolean isTeamAKO(){return battleSnapshot.getBattle().isTeamAKO();}
	public boolean isTeamBKO(){return battleSnapshot.getBattle().isTeamBKO();}

	public void switchBugemonA(){battleSnapshot.switchSelfBugemonAuto();}
	public void switchBugemonB(){battleSnapshot.switchOpponentBugemonAuto();}

}
