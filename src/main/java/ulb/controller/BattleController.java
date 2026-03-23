package ulb.controller;

import java.io.IOException;
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
import ulb.view.windows.BattleModeWindow;
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
import ulb.model.reward.Reward;

public class BattleController {
	private Player player;
	private WindowContainer windowContainer;
	private Battle battle;
	private boolean isTeamA;
	private int floorNumber = 1;
	private boolean isBossFight = false;

	private Vector<Reward> rewards;

	public BattleController(){
		this.windowContainer = new WindowContainer();
	}

	public BattleController(Player player, Battle battle, boolean isTeamA) {
		this.player = player;
		this.battle = battle;
		this.isTeamA = isTeamA;
	}

	/**
	 * Checks if an item can be used or not given the stats of the bugemon
	 *
	 * @param item the item that needs to be checked
	 * @return if the item can be used or not (boolean)
	 */
	public boolean checkItem(Item item) {
		return this.battle.checkItem(item, this.isTeamA);
	}


	/**
	 * Gives the current active Bugemon.
	 *
	 * @return the active Bugemon
	 */
	public Bugemon getActiveBugemonSelf() {
		return this.battle.getActiveBugemon(this.battle.getTeamLabel(this.isTeamA));
		// if (this.isTeamA) {
		// 	return battle.getActiveBugemonA();
		// }
		// else {
		// 	return battle.getActiveBugemonB();
		// }
	}

	/**
	 * Gives the opponent's team current active Bugemon
	 *
	 * @return the opponent's active Bugemon
	 */
	public Bugemon getActiveBugemonOpponent() {
		return this.battle.getActiveBugemon(this.battle.getTeamLabel(!this.isTeamA));
		// if (this.isTeamA){
		// 	return this.battle.getActiveBugemonB();
		// }
		// return this.battle.getActiveBugemonA();
	}

	public Player getPlayer() {
		return this.player;
	}

	public Team getTeam(){
		return this.battle.getTeam(this.battle.getTeamLabel(this.isTeamA));
		// if (isTeamA){
		// 	return this.battle.getTeamA();
		// }
		// return this.battle.getTeamB();
	}

	public void useAction(Action action) {
		this.battle.chooseAction(action, isTeamA);
	}

	public Vector<Action> getAvailableAction(){ return this.battle.getAvailableActions(isTeamA); }

	public List<Bugemon> getAvailableBugemons(){
		return this.battle.getAvailableBugemons(isTeamA);
	}

	public boolean isGameFinished(){
		return this.battle.isGameFinished();
	}

	public BattleState getState(){
		return this.battle.getState(isTeamA);
	}

	/**
	 * Get the possible random rewards from Battle and create a copy to avoid cheating
	 * @param bugemonTarget the bugemon targeted by the reward
	 * @return a vector of the copied rewards
	 */
	public Vector<Reward> getRewards(Bugemon bugemonTarget){
		this.rewards = battle.computeRewards(bugemonTarget);
		Vector<Reward> vector = new Vector<>();
		for (Reward r : this.rewards){
			vector.add(new Reward(r));
		}
		return vector;
	}

	public boolean applyReward(Bugemon bugemonTarget, Reward reward){
		for (Reward r : this.rewards) {
			if (r.getStats().equals(reward.getStats()) && r.getBugemon().equals(bugemonTarget)){
				r.applyReward();
				return true;
			}
		}
		return false;
	}

	/**
	 * Gets the effectiveness factor of the current ability
	 *
	 * @param ability the ability whose type effectiveness is evaluated
	 * @return the effectiveness message (or null if the effectiveness is normal)
	 */
	public String getEffectiveness(Ability ability) {
		Bugemon oppositeBugemon = getActiveBugemonOpponent();
		return ability.getEffectivenessMessage(oppositeBugemon);
	}

	public int getHpAfterFirstActionSelf() {
		return isTeamA ? battle.getHpAfterFirstActionA() : battle.getHpAfterFirstActionB();
	}

	public int getHpAfterFirstActionOpponent() {
		return isTeamA ? battle.getHpAfterFirstActionB() : battle.getHpAfterFirstActionA();
	}

	public List<String> getLogMsg() {
		return this.battle.getLogMsg();
	}

	public void clearLogMsg() {
		this.battle.clearLogMsg();
	}

	public int getTotalXP() { 
		return this.battle.computeTotalXP(!this.isTeamA); // TO REFACTOR
	}
}
