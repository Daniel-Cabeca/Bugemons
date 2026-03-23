package ulb.model.battle;

import java.lang.IllegalArgumentException;

import ulb.model.team.Team;
import ulb.model.Player;
import ulb.model.type.Effectiveness;
import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.item.Item;
import ulb.model.effect.Effect;
import ulb.controller.action.*; 
import ulb.model.reward.Reward;
import ulb.model.reward.RewardType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;


public class Battle {
	private final int XP_COEF = 30;

	private Player playerA;
	private Player playerB;

	private Team teamA;
	private Team teamB;
	private Bugemon activeBugemonA;
	private Bugemon activeBugemonB;
	private Vector<Bugemon> participantsA;
	private Vector<Bugemon> participantsB;

	private BattleState stateA;
	private BattleState stateB;
	private Action actionA = null;
	private Action actionB = null;

	private boolean isBossBattle = false;
	private int floorNumber = 1;

	private boolean gameFinished = false;

	private List<String> logMsg;
	private List<ActiveEffect> activeEffects;

	private int hpAfterFirstActionA = -1;
	private int hpAfterFirstActionB = -1;

	public enum TeamLabel {
		TEAM_A,
		TEAM_B
	}

	public Battle(Team teamA, Team teamB, Player playerA, Player playerB) {
		this.playerA = playerA;
		this.playerB = playerB;
		this.teamA = teamA;
		this.teamB = teamB;
		this.activeBugemonA = this.teamA.getMembers().get(0);
		this.activeBugemonB = this.teamB.getMembers().get(0);
		this.stateA = BattleState.INGAME;
		this.stateB = BattleState.INGAME;
		this.participantsA = new Vector<>() {};
		this.participantsB = new Vector<>() {};
		this.participantsA.add(activeBugemonA);
		this.participantsB.add(activeBugemonB);
		this.logMsg = new ArrayList<>();
		this.activeEffects = new ArrayList<>();
	}

	public Battle(Team teamA, Team teamB, Player playerA) {
		this(teamA, teamB, playerA, new Player());
	}

	public Battle(Team teamA, Team teamB, Bugemon activeBugemonA, Bugemon activeBugemonB) {
		this.teamA = teamA;
		this.teamB = teamB;
		this.activeBugemonA = activeBugemonA;
		this.activeBugemonB = activeBugemonB;
		this.stateA = BattleState.INGAME;
		this.stateB = BattleState.INGAME;
		this.activeEffects = new ArrayList<>();
	}

	public Team getTeamA() {return this.teamA;}
	public Team getTeamB() {return this.teamB;}
	public Bugemon getActiveBugemonA() {return this.activeBugemonA;}
	public Bugemon getActiveBugemonB() {return this.activeBugemonB;}

	public List<ActiveEffect> getActiveEffects() { return this.activeEffects; }

	public BattleState getState(boolean isTeamA) {
		if (isTeamA){
			return this.stateA;
		}
		return this.stateB;
	}

	private void setActiveBugemonA(Bugemon bugemon) {
		this.activeBugemonA = bugemon;
		if (!participantsA.contains(bugemon)){
			participantsA.add(bugemon);
		}
	}
	private void setActiveBugemonB(Bugemon bugemon) {
		this.activeBugemonB = bugemon;
		if (!participantsB.contains(bugemon)){
			participantsB.add(bugemon);
		}
	}

	public void setOwnActiveBugemon(TeamLabel teamLabel, Bugemon bugemon) {
		switch(teamLabel) {
			case TEAM_A:
				this.setActiveBugemonA(bugemon);
				break;

			case TEAM_B:
				this.setActiveBugemonB(bugemon);
				break;

			default:
				throw new IllegalArgumentException("This team label is not handled.");
		}
	}

	private void setState(BattleState state, TeamLabel team) {
		if (team == TeamLabel.TEAM_A) {
			this.stateA = state;
		} else {
			this.stateB = state;
		}
    }

	public boolean isBugemonAKO() {
		   return  getActiveBugemonA().isKO();
	}

	public boolean isBugemonBKO() {
		return  getActiveBugemonB().isKO();
	}

	public boolean isTeamAKO() { // victory
		return getTeamA().checkTeamKO();
	}

	public boolean isTeamBKO() { // defeat
		return getTeamB().checkTeamKO();
	}

	public void setFloorNumber(int floorNumber){ this.floorNumber = floorNumber; }
	public void enableBossBattle() { this.isBossBattle = true; }

	public TeamLabel getTeamLabel(boolean isTeamA){
		if (isTeamA){
			return TeamLabel.TEAM_A;
		}
		return TeamLabel.TEAM_B;
	}

	public Team getOwnTeam(TeamLabel teamLabel) {
		switch(teamLabel) {
			case TEAM_A:
				return this.teamA;

			case TEAM_B:
				return this.teamB;

			default:
				throw new IllegalArgumentException("This team label is not handled.");
		}
	}

	public Bugemon getOwnActiveBugemon(TeamLabel teamLabel) {
		switch(teamLabel) {
			case TEAM_A:
				return this.activeBugemonA;

			case TEAM_B:
				return this.activeBugemonB;

			default:
				throw new IllegalArgumentException("This team label is not handled.");
		}
	}

	public Bugemon getOppositeActiveBugemon(TeamLabel teamLabel) {
		switch(teamLabel) {
			case TEAM_A:
				return this.activeBugemonB;

			case TEAM_B:
				return this.activeBugemonA;

			default:
				throw new IllegalArgumentException("This team label is not handled.");
		}
	}

	/**
	 * get the team that have the initiative
	 * @return the TeamLabel of the first team to play
	 */
	public TeamLabel getFirstTeamToPlay(){

		if (this.actionA instanceof Swap && !(this.actionB instanceof Swap)) {
			return TeamLabel.TEAM_A;
		} else if (this.actionB instanceof Swap && !(this.actionA instanceof Swap)) {
			return TeamLabel.TEAM_B;
		}

		if (getActiveBugemonA().checkInitiative(getActiveBugemonB())){
			return TeamLabel.TEAM_A;
		}
		return TeamLabel.TEAM_B;
	}

	/**
	 * Checks if an item can be used or not given the stats of the bugemon
	 *
	 * @param item the item that needs to be checked
	 * @return if the item can be used or not (boolean)
	 */
	public boolean checkItem(Item item, boolean isTeamA) {
		if (item.getEffect().getType().equals(Effect.EffectType.HEAL)) {
			return this.getOwnActiveBugemon(this.getTeamLabel(isTeamA)).hasHPDecreased();
		}

		return true;
	}

	/**
	 * Removes an item that has been used from the inventory of the team
	 * @param team the team from which the item is removed
	 * @param item the item which is removed from the inventory
	 */
	private void removeUsedItemFromInventory(TeamLabel team, Item item) {
		switch (team) {
			case TEAM_A:
				playerA.getInventory().removeItem(item);
				break;

			case TEAM_B:
				playerB.getInventory().removeItem(item);
				break;

			default:
				break;
		}
	}

	/**
	 * Uses an item and manages its removal from the inventory
	 * @param item the item which is used
	 * @param team the team that uses the item
	 */
	private void useItem(Item item, TeamLabel team){
		item.use(this, team);
		removeUsedItemFromInventory(team, item);
		logMsg.add("L'objet " + item.getName() + "a été utilisé.");
	}

	/**
	 * Swaps the active Bugemon to another one
	 * @param target the Bugemon which will replace the current active Bugemon
	 * @param team the team on which the active Bugemon will be replaced
	 */
	private void swap(Bugemon target, TeamLabel team){
		if (team == TeamLabel.TEAM_A && checkSwappableBugemon(target, team)){
			setActiveBugemonA(target);
			logMsg.add("Tu as envoyé " + target.getName() + "!");
		} else if (team == TeamLabel.TEAM_B && checkSwappableBugemon(target, team)){
			setActiveBugemonB(target);
			logMsg.add("L'adversaire a envoyé " + target.getName() + "!");
		}
	}

	/**
	 * Returns the next available non-active Bugemon when switching.
	 * @param team the team whose Bugemons are being considered
	 * @return the next available non-active Bugemon or null if none available
	 */
	public Bugemon getOwnNextBugemon(TeamLabel team) {
		return this.getOwnTeam(team).getNextBugemon(this.getOwnActiveBugemon(team));
	}

	/**
	 * Returns all available actions based on current game state
	 * @param isTeamA if the current team is A or B
	 * @return the currently available actions
	 */
	public Vector<Action> getAvailableActions(boolean isTeamA) {
		Vector<Action> actions = new Vector<Action>();

		if (gameFinished){
			return actions;
		}

		switch (this.getState(isTeamA)) {
			case INGAME:
				actions.add(new UseAbility());
				actions.add(new Run());
				actions.add(new Swap());
				actions.add(new UseItem());
				break;

			case SWAPPING:
				actions.add(new Swap());
				break;

			case LOST:
				break;

			case WON:
				break;

			case WAITING:
				break;

			default:
				break;
		}
		return actions;
	}

	/**
	 * Applies an action on a team
	 * @param action the action being applied
	 * @param team the team on which the action is applied
	 * @return boolean indicating if the action succeeded
	 */
	private boolean applyAction(Action action, TeamLabel team){
		if (action instanceof UseAbility useAbilityAction) {
			Bugemon oppositeBugemon = this.getOppositeActiveBugemon(team);

			if (!oppositeBugemon.isKO()) {
				useAbilityAction.getAbility().use(this, team);
			}

		} else if (action instanceof Swap swapAction) {
			if (checkSwappableBugemon(swapAction.getToSwap(), team)){
				this.swap(swapAction.getToSwap(), team);
			} else {
				return false;
			}

		} else if (action instanceof Run) {

			this.setState(BattleState.LOST, team);

		} else if (action instanceof UseItem useItemAction) {
			this.useItem(useItemAction.getItem(), team);
		}

		return true;
	}

	/**
	 * Registers an action done by a specific team and calls the handling of a round when both teams are waiting
	 * @param action the action to be registered
	 * @param ownTeam the team which registers the action
	 * @param oppositeTeam the opposite team, whose state changes if ownTeam swaps active Bugemons
	 * @param ownState the current state of the team registering the action
	 */
	private void registerAction(Action action, TeamLabel ownTeam, TeamLabel oppositeTeam, BattleState ownState) {
		switch (ownState) {
			case INGAME:
				if (action instanceof UseItem useItemAction
						&& useItemAction.getItem().getEffect().getType() == Effect.EffectType.SWITCH) {
					applyAction(action, ownTeam);
				} else {
					if (ownTeam == TeamLabel.TEAM_A) {
						this.actionA = action;
					} else {
						this.actionB = action;
					}
					setState(BattleState.WAITING, ownTeam);
				}
				break;

			case SWAPPING:
				if (ownTeam == TeamLabel.TEAM_A) {
						this.actionA = action;
				} else {
					this.actionB = action;
				}
				if (action instanceof Swap){
					if (applyAction(action, ownTeam)){
						setState(BattleState.INGAME, ownTeam);
						setState(BattleState.INGAME, oppositeTeam);
					}
				}
				break;
			default:
				break;
		}
		if (this.stateA == BattleState.WAITING && this.stateB == BattleState.WAITING){
			handleRound();
		}
	}

	/**
	 * Registers an action of a specific team
	 * @param action the action to be registered
	 * @param isTeamA the team registering the action
	 */
	public synchronized void setAction(Action action, boolean isTeamA){
		if (isTeamA){
			registerAction(action, TeamLabel.TEAM_A, TeamLabel.TEAM_B, stateA);
		} else {
			registerAction(action, TeamLabel.TEAM_B, TeamLabel.TEAM_A, stateB);
		}
	}

	/**
	 * handle the round of one of the two players
	 * @param playerTeam the player who plays now
	 * @return a boolean depending on if the turn continues
	 */
	private boolean handlePlayerTurn(TeamLabel playerTeam){
		Action currentAction = this.actionA;
		if (playerTeam == TeamLabel.TEAM_B){
			currentAction = this.actionB;
		}

		this.applyAction(currentAction, playerTeam);

		if (handleActionFinished(playerTeam)){
			tickActiveEffects();
			if (gameFinished){
				handleBattleEnd();
			}
			return false;
		}
		return true;
	}

	/**
	 * Handles one round of the battle
	 */
	private void handleRound(){
		// checks whose action should be executed first and applies it
		hpAfterFirstActionA = -1;
		hpAfterFirstActionB = -1;

		TeamLabel firstPlayer;
		TeamLabel secondPlayer;

		firstPlayer = this.getFirstTeamToPlay();
		secondPlayer = TeamLabel.TEAM_A;
		if (firstPlayer == TeamLabel.TEAM_A){
			secondPlayer = TeamLabel.TEAM_B;
		}
		
		if (! this.handlePlayerTurn(firstPlayer)){
			return;
		}

		hpAfterFirstActionA = activeBugemonA.getHp();
		hpAfterFirstActionB = activeBugemonB.getHp();
		logMsg.add(null); // separator between first and second action messages 
		
		if (! this.handlePlayerTurn(secondPlayer)){
			return;
		}

		tickActiveEffects();
		setState(BattleState.INGAME, TeamLabel.TEAM_A);
		setState(BattleState.INGAME, TeamLabel.TEAM_B);
	}

	/**
	 * Decrements TTL of all active TOUR effects and reverts those that have expired.
	 */
	private void tickActiveEffects() {
		List<ActiveEffect> expired = new ArrayList<>();
		for (ActiveEffect ae : activeEffects) {
			ae.ttl--;
			if (ae.ttl <= 0) {
				Stats revert = new Stats(-ae.delta.hp, -ae.delta.attack, -ae.delta.defense, -ae.delta.initiative);
				ae.target.changeFightStats(revert);
				logMsg.add("L'effet de " + ae.itemName + " sur " + ae.target.getName() + " s'est dissipé!");
				expired.add(ae);
			}
		}
		activeEffects.removeAll(expired);
	}


	/**
	 * Check if the round is finished and set the states depending on that
	 * @param previousActiveTeam the team who just finished the round
	 * @return a boolean depending on if the round is finished
	 */
	private boolean handleActionFinished(TeamLabel previousActiveTeam){
		switch (previousActiveTeam) {
			case TEAM_A:
				if (this.isTeamBKO()){
					setState(BattleState.LOST, TeamLabel.TEAM_B);
					setState(BattleState.WON, TeamLabel.TEAM_A);
					this.gameFinished = true;
				} else if (this.isBugemonBKO()){
					logMsg.add(activeBugemonB.getName() + " est KO!");
					if (hpAfterFirstActionA == -1) {
						hpAfterFirstActionA = activeBugemonA.getHp();
						hpAfterFirstActionB = 0;
					}
					logMsg.add(null);
					Bugemon nextB = teamB.getNextBugemon(activeBugemonB);
					if (nextB != null) {
						setActiveBugemonB(nextB);
						logMsg.add("L'adversaire a envoyé " + nextB.getName() + "!");
					}
					setState(BattleState.INGAME, TeamLabel.TEAM_A);
					setState(BattleState.INGAME, TeamLabel.TEAM_B);
				} else {
					return false;
				}
				break;

			case TEAM_B:
				if (this.isTeamAKO()){
					setState(BattleState.LOST, TeamLabel.TEAM_A);
					setState(BattleState.WON, TeamLabel.TEAM_B);
					this.gameFinished = true;
				} else if (this.isBugemonAKO()){
					setState(BattleState.SWAPPING, TeamLabel.TEAM_A);
				} else {
					return false;
				}
				break;
			default:
				break;
		}
		return true;
	}

	/**
	 * Handles the end of the battle
	 */
	private void handleBattleEnd(){
		List<Bugemon> winners = this.participantsA;
		Team losers = this.teamB;
		if (this.stateB == BattleState.WON){
			winners = this.participantsB;
			losers = this.teamA;
		}

		int gainedXP = computeTotalXP(losers);
		for (Bugemon b : winners){
			b.gainXp(gainedXP / winners.size());
		}

		this.resetAllFightStats();
	}

	/**
	 * Computes total XP
	 * @param losers the vanquished team
	 * @return the gained XP
	 */
	public int computeTotalXP(Team losers){
		int boss_multiplicator = 1;
		if (isBossBattle){
			boss_multiplicator = 2;
		}
		return XP_COEF * floorNumber * boss_multiplicator * losers.size();
	}

	/**
	 * Returns the available Bugemons
	 * @param isTeamA if the current team is A or B
	 * @return the available Bugemons
	 */
	public List<Bugemon> getAvailableBugemons(boolean isTeamA){
		Team team = teamA;
		if (!isTeamA){
			team = teamB;
		}
		return team.getBugemonsAlive();
	}

	public Vector<Reward> computeRewards(Bugemon bugemonTarget){
		Vector<Reward> rewards = new Vector<>();
		for (int i=0; i < 3; i++){
			Reward r = new Reward(bugemonTarget);
			r.choseType(RewardType.COMBINATION);
			rewards.add(r);
		}
		return rewards;
	}

	public boolean isGameFinished() {
		return this.gameFinished;
	}

	/**
	 * Resets all fight stats
	 */
	private void resetAllFightStats(){
		for (Bugemon b : this.teamA.getMembers()){
			b.removeStatsDebuffs();
		}

		for (Bugemon b : this.teamB.getMembers()){
			b.removeStatsDebuffs();
		}
	}

	/**
	 * Checks if the Bugemon trying to be swapped can be used
	 * @param bugemon the Bugemon trying to be swapped
	 * @param team the team on which the Bugemon should be
	 * @return true if the Bugemon is available, false otherwise
	 */
	public boolean checkSwappableBugemon(Bugemon bugemon, TeamLabel team){
		return this.getOwnTeam(team).isBugemonOK(bugemon);
	}

	public int getHpAfterFirstActionA() { return hpAfterFirstActionA; }
	public int getHpAfterFirstActionB() { return hpAfterFirstActionB; }

	public List<String> getLogMsg() { return logMsg; } 

	/** 
	 * Clears the current log message
	 */
	public void clearLogMsg() { 
		logMsg.clear();
	}
}
