package ulb.model.battle;

import ulb.model.team.Team;
import ulb.model.Player;
import ulb.model.type.Effectiveness;
import ulb.model.ability.Ability;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.item.Item;
import ulb.model.Effect;
import ulb.controller.action.*; 

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

	public TeamLabel checkInitiative(){
		if (getActiveBugemonA().getFightStats().getInitiative() > getActiveBugemonB().getFightStats().getInitiative()){
			return TeamLabel.TEAM_A;
		}
		else if(getActiveBugemonA().getFightStats().getInitiative() == getActiveBugemonB().getFightStats().getInitiative()) {
			 Random rand = new Random();
			 int i = rand.nextInt(2);
			 if (i == 0) {
				 return TeamLabel.TEAM_A;
			 }
			 else  {
				 return TeamLabel.TEAM_B;
			}
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
		if (item.getEffect().getType().equals(Effect.EffectType.SOIN)) {
			if (isTeamA) {
				int baseHp = getActiveBugemonA().getBaseStats().getHp();
				int fightHP = getActiveBugemonA().getHp();
				if (baseHp == fightHP) {
					return false;
				}
			} else {
				int baseHp = getActiveBugemonB().getBaseStats().getHp();
				int fightHP = getActiveBugemonB().getHp();
				if (baseHp == fightHP) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Compute the damage based on the complete formula
	 *
	 * @param offensive The bugemon that uses the ability
	 * @param defensive The bugemon targeted by the ability
	 * @param ability Ability from attacker used against defender
	 * @return the computed damage based on the formula
	 */
	private int computeDamage(Bugemon offensive, Bugemon defensive, Ability ability) {
		float attackValue = offensive.getFightStats().attack;
		float defenseValue = defensive.getFightStats().defense;

		float attackFactor = (100 + attackValue) / 100f;
		float defenseFactor = 100f / (100 + defenseValue);

		float baseDamage = attackFactor * defenseFactor * ability.getPower();

		// Bugemon d'un type quelconque peut avoir des attaques de type t.q. type d'attaque ≠ type du pokemon
		float typeFactor = Effectiveness.getFactor(ability.getType(), defensive.getType());

		float criticalHitFactor = 1f;
		if (Math.random() <= 0.1){
			criticalHitFactor = 1.5f;
		}

		return Math.round(baseDamage * typeFactor * criticalHitFactor);
	}

	/**
	 * Gets the effectiveness factor of the current ability
	 *
	 * @param ability the ability whose type effectiveness is evaluated
	 * @return the effectiveness message (or null if the effectiveness is normal)
	 */
	public String getEffectiveness(Ability ability, Bugemon opponent) {
        float factor = Effectiveness.getFactor(ability.getType(), opponent.getType());
		String message;
		if (factor > 1) {
			message = "Super efficace!";
		} else if (factor < 1) {
			message = "Pas très efficace!";
		} else {
			message = null;
		}
		return message;
	}


	/**
	 * Use the given ability against the opposing active Bugemon
	 *
	 * @param ability the ability that is used
	 * @param team the team using the ability
	 */
	private void useAbility(Ability ability, TeamLabel team) {
		Bugemon offensive;
		Bugemon defensive;
		if (team == TeamLabel.TEAM_A){
			offensive = this.getActiveBugemonA();
			defensive = this.getActiveBugemonB();
		} else {
			offensive = this.getActiveBugemonB();
			defensive = this.getActiveBugemonA();
		}

		if (offensive.isKO()){
			return;
		}

		int abilityDamage = computeDamage(offensive, defensive, ability);
		Stats damage = new Stats(-abilityDamage, 0, 0, 0);
		defensive.changeFightStats(damage);

		logMsg.add(offensive.getName() + " a utilisé " + ability.getName() + ". " + defensive.getName() + " perd " +
				abilityDamage + " PV!");

		String effectiveness = getEffectiveness(ability, defensive);
		if (effectiveness != null) {
			logMsg.add(effectiveness);
		}

		if (ability.getEffect() != null) {
			applyEffect(team, ability.getEffect());
		}
	}

	/**
	 * Applies an effect on the active Bugemon of the opposite team
	 * 
	 * @param team the team which applies the effect
	 * @param effect the applied effect
	 * @param targets list of bugemons which have an applied effect
	 */
	private void applyEffectOnOppositeActiveBugemon(TeamLabel team, Effect effect, List<Bugemon> targets) {
		switch (team) {
			case TEAM_A:
				effect.apply(this.activeBugemonB);
				targets.add(this.activeBugemonB);
				break;

			case TEAM_B:
				effect.apply(this.activeBugemonA);
				targets.add(this.activeBugemonA);
				break;

			default:
				break;
		}
	}

	/**
	 * Applies an effect on the active Bugemon of the user's team
	 * 
	 * @param team the team which applies the effect
	 * @param effect the applied effect
	 * @param targets list of bugemons which have an applied effect
	 */
	private void applyEffectOnOwnActiveBugemon(TeamLabel team, Effect effect, List<Bugemon> targets) {
		switch (team) {
			case TEAM_A:
				effect.apply(this.activeBugemonA);
				targets.add(this.activeBugemonA);
				break;

			case TEAM_B:
				effect.apply(this.activeBugemonB);
				targets.add(this.activeBugemonB);
				break;

			default:
				break;
		}
	}

	/**
	 * Applies an effect on an entire team
	 * 
	 * @param team the team which applies the effect
	 * @param effect the applied effect
	 * @param targets list of bugemons which have an applied effect
	 */
	private void applyEffectOnEntireTeam(TeamLabel team, Effect effect, List<Bugemon> targets) {
		switch (team) {
			case TEAM_A:
				for (Bugemon b : this.teamA.getMembers()){
					effect.apply(b);
					targets.add(b);
				}
				break;

			case TEAM_B:
				for (Bugemon b : this.teamB.getMembers()){
					effect.apply(b);
					targets.add(b);
				}
				break;

			default:
				break;
		}
	}

	/**
	 * Applies the switching effect on a team
	 * @param team the team on which the effect applies
	 */
	private void applySwitchEffect(TeamLabel team) {
		if (team == TeamLabel.TEAM_A) {
			Bugemon nextBugemon = getNextBugemon(this.teamA, this.activeBugemonA);
			if (nextBugemon != null) {
				setActiveBugemonA(nextBugemon);
			}
		} else {
			Bugemon nextBugemon = getNextBugemon(this.teamB, this.activeBugemonB);
			if (nextBugemon != null) {
				setActiveBugemonB(nextBugemon);
			}
		}
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
	 * Applies the effect of an item of the given team 
	 * @param team the team which applies the effect
	 * @param effect the applied effect
	 */
	private void applyEffect(TeamLabel team, Effect effect) {
		List<Bugemon> targets = new ArrayList<>();

		// apply effect on target (all except switch)
		if (effect.getTarget().equals(Effect.EffectTarget.ADVERSAIRE)) {
			applyEffectOnOppositeActiveBugemon(team, effect, targets);

		} else if (effect.getTarget().equals(Effect.EffectTarget.LANCEUR)) {
			applyEffectOnOwnActiveBugemon(team, effect, targets);
		
		} else {
			applyEffectOnEntireTeam(team, effect, targets);
		}

		// applies switch effect
		if (effect.getType().equals(Effect.EffectType.SWITCH)) {
			applySwitchEffect(team);
		}

		// creates active effect for targeted Bugemons
		if (effect.getDuration() == Effect.EffectDuration.TOUR
				&& effect.getType() == Effect.EffectType.STAT_MODIFIER) {
			Stats delta = effect.buildStatsChange();
			for (Bugemon target : targets) {
				activeEffects.add(new ActiveEffect(target, delta, 1));
			}
		}
	}

	/**
	 * Uses an item and manages its removal from the inventory
	 * @param item the item which is used
	 * @param team the team that uses the item
	 */
	private void useItem(Item item, TeamLabel team){
		applyEffect(team, item.getEffect());
		removeUsedItemFromInventory(team, item);
		logMsg.add("L'objet " + item.getName() + "a été utilisé.");
	}

	/**
	 * Swaps the active Bugemon to another one
	 * @param target the Bugemon which will replace the current active Bugemon
	 * @param team the team on which the active Bugemon will be replaced
	 */
	private void swap(Bugemon target, TeamLabel team){
		if (team == TeamLabel.TEAM_A && teamA.contains(target)){
			setActiveBugemonA(target);
			logMsg.add("Tu as envoyé " + target.getName() + "!");
		} else if (team == TeamLabel.TEAM_B && teamB.contains(target)){
			setActiveBugemonB(target);
			logMsg.add("L'adversaire a envoyé " + target.getName() + "!");
		}
	}

	/**
	 * Returns the next available non-active Bugemon when switching
	 * @param team the team whose Bugemons are being considered
	 * @param active the current active Bugemon
	 * @return the next available non-active Bugemon or null if none available
	 */
	private Bugemon getNextBugemon(Team team, Bugemon active) {
		java.util.List<Bugemon> members = team.getMembers();
		int currentIndex = members.indexOf(active);

		if (currentIndex == -1 || members.size() <= 1) {
			return null;
		}

		for (int i = 1; i < members.size(); i++) {
			int nextIndex = (currentIndex + i) % members.size();
			Bugemon candidate = members.get(nextIndex);
			if (!candidate.isKO()) {
				return candidate;
			}
		}

		return null;
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

			this.useAbility(useAbilityAction.getAbility(), team);

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
	public void registerAction(Action action, TeamLabel ownTeam, TeamLabel oppositeTeam, BattleState ownState) {
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
	 * Handles one round of the battle
	 */
	private void handleRound(){
		// checks whose action should be executed first and applies it
		Action currentAction = this.actionA;
		TeamLabel firstPlayer = this.checkInitiative();
		if (firstPlayer == TeamLabel.TEAM_B){
			currentAction = this.actionB;
		}
		this.applyAction(currentAction, firstPlayer);

		// updates the ttl of active items or handles the end of the battle
		if (handleActionFinished(firstPlayer)){
			tickActiveEffects();
			if (gameFinished){
				handleBattleEnd();
			}
			return;
		}

		// applies the action of the second player
		TeamLabel secondPlayer = TeamLabel.TEAM_A;
		currentAction = this.actionA;
		if (firstPlayer == TeamLabel.TEAM_A){
			secondPlayer = TeamLabel.TEAM_B;
			currentAction = this.actionB;
		}
		this.applyAction(currentAction, secondPlayer);

		// updates the ttl of active items or handles the end of the battle
		if (handleActionFinished(secondPlayer)){
			tickActiveEffects();
			if (gameFinished){
				handleBattleEnd();
			}
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
					setState(BattleState.SWAPPING, TeamLabel.TEAM_B);
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
	public Vector<Bugemon> getAvailableBugemons(boolean isTeamA){
		Team team = teamA;
		if (!isTeamA){
			team = teamB;
		}
		Vector<Bugemon> availableBugemons = new Vector<>();

		for (Bugemon b : team.getMembers()){
			if (!b.isKO()){
				availableBugemons.add(b);
			}
		}
		return availableBugemons;
	}

	public boolean isGameFinished(){ return this.gameFinished; }

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
		switch (team) {
			case TEAM_A:
				return this.teamA.contains(bugemon) && bugemon.getHp() > 0;

			case TEAM_B:
				return this.teamB.contains(bugemon) && bugemon.getHp() > 0;

			default:
				break;
		}
		return false;
	}

	public List<String> getLogMsg() { return logMsg; }

	/** 
	 * Clears the current log message
	 */
	public void clearLogMsg() { 
		logMsg.clear();
	}
}
