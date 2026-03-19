package ulb.controller;

import java.util.List;
import java.util.Vector;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.BattleState;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.view.handler.WindowContainer;
import ulb.controller.action.*;
import ulb.model.Player;
import ulb.model.item.Item;

public class BattleController {
	private Player player;
	private WindowContainer windowContainer;
	private Battle battle;
	private boolean isTeamA;

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
		return this.battle.checkItem(item, isTeamA);
	}


	/**
	 * Gives the current active Bugemon.
	 *
	 * @return the active Bugemon
	 */
	public Bugemon getActiveBugemonSelf() {
		if (this.isTeamA) {
			return battle.getActiveBugemonA();
		}
		else {
			return battle.getActiveBugemonB();
		}
	}

	/**
	 * Gives the opponent's team current active Bugemon
	 *
	 * @return the opponent's active Bugemon
	 */
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

	public void useAction(Action action) {
		this.battle.setAction(action, isTeamA);
	}

	public Vector<Action> getAvailableAction(){ return this.battle.getAvailableActions(isTeamA); }

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
}
