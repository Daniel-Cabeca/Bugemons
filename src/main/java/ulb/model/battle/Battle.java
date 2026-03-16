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

	public TeamLabel checkInitiave(){
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
	}

	private void useItem(Item item, TeamLabel team){

		if (!checkItem(item, team == TeamLabel.TEAM_A)) {
			return;
		}

		List<Bugemon> targets = new ArrayList<>();

		if (item.getTarget().equals(Effect.EffectTarget.ADVERSAIRE)) {
			switch (team) {
				case TEAM_A:
					item.use(this.activeBugemonB);
					targets.add(this.activeBugemonB);
					logMsg.add("Tu as utilisé " + item.getName() + " sur " + activeBugemonB.getName() + "!");
					break;

				case TEAM_B:
					item.use(this.activeBugemonA);
					targets.add(this.activeBugemonA);
					logMsg.add("L'adversaire a utilisé " + item.getName() + " sur " + activeBugemonA.getName() + "!");
					break;

				default:
					break;
			}

		} else if (item.getTarget().equals(Effect.EffectTarget.LANCEUR)) {
			switch (team) {
				case TEAM_A:
					item.use(this.activeBugemonA);
					targets.add(this.activeBugemonA);
					logMsg.add("Tu as utilisé " + item.getName() + " sur " + activeBugemonA.getName() + "!");
					break;

				case TEAM_B:
					item.use(this.activeBugemonB);
					targets.add(this.activeBugemonB);
					logMsg.add("L'adversaire a utilisé " + item.getName() + " sur " + activeBugemonB.getName() + "!");
					break;

				default:
					break;
			}
		} else {
			switch (team) {
				case TEAM_A:
					for (Bugemon b : this.teamA.getMembers()){
						item.use(b);
						targets.add(b);
					}
					logMsg.add("Tu as utilisé " + item.getName() + " sur toute ton équipe!");
					break;

				case TEAM_B:
					for (Bugemon b : this.teamB.getMembers()){
						item.use(b);
						targets.add(b);
					}
					logMsg.add("L'adversaire a utilisé " + item.getName() + " sur toute son équipe!");
					break;

				default:
					break;
			}
		}

		if (item.getEffect().getDuration() == Effect.EffectDuration.TOUR
				&& item.getEffect().getType() == Effect.EffectType.STAT_MODIFIER) {
			Stats delta = item.getEffect().buildStatsChange();
			for (Bugemon target : targets) {
				activeEffects.add(new ActiveEffect(target, delta, 1));
			}
		}

		if (item.getEffect().getType().equals(Effect.EffectType.SWITCH)) {
			if (team == TeamLabel.TEAM_A) {
				Bugemon switchedBugemon = this.activeBugemonA;
				Bugemon nextBugemon = getNextBugemon(this.teamA, this.activeBugemonA);
				if (nextBugemon != null) {
					setActiveBugemonA(nextBugemon);
					logMsg.add("Tu as échangé " + switchedBugemon.getName() + " avec " + nextBugemon.getName() + " en utilisant " + item.getName() + "!");
				}
			} else {
				Bugemon switchedBugemon = this.activeBugemonB;
				Bugemon nextBugemon = getNextBugemon(this.teamB, this.activeBugemonB);
				if (nextBugemon != null) {
					setActiveBugemonB(nextBugemon);
					logMsg.add("L'adversaire a échangé " + switchedBugemon + " avec " + nextBugemon.getName() + " en utilisant " + item.getName() + "!");
				}
			}
		}

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

	private void swap(Bugemon target, TeamLabel team){
		if (team == TeamLabel.TEAM_A && teamA.contains(target)){
			setActiveBugemonA(target);
			logMsg.add("Tu as envoyé " + target.getName() + "!");
		} else if (team == TeamLabel.TEAM_B && teamB.contains(target)){
			setActiveBugemonB(target);
			logMsg.add("L'adversaire a envoyé " + target.getName() + "!");
		}
	}

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

	public Vector<Action> getAvailableActions(boolean isTeamA) {
		//this.hasTourEnded = false;

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

	private boolean applyAction(Action action, TeamLabel team){
		if (action instanceof UseAbility useAbilityAction) {

			this.useAbility(useAbilityAction.getAbility(), team);

		} else if (action instanceof Swap swapAction) {
			if (checkSwappebleBugemon(swapAction.getToSwap(), team)){
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

	public synchronized void setAction(Action action, boolean isTeamA){
		if (isTeamA){
			switch (this.stateA) {
				case INGAME:
					if (action instanceof UseItem useItemAction
							&& useItemAction.getItem().getEffect().getType() == Effect.EffectType.SWITCH) {
						applyAction(action, TeamLabel.TEAM_A);
					} else {
						this.actionA = action;
						setState(BattleState.WAITING, TeamLabel.TEAM_A);
					}
					break;

				case SWAPPING:
					this.actionA = action;
					if (action instanceof Swap){
						if (applyAction(action, TeamLabel.TEAM_A)){
							setState(BattleState.INGAME, TeamLabel.TEAM_A);
							setState(BattleState.INGAME, TeamLabel.TEAM_B);
						}
					}
					break;
				default:
					break;
			}
		} else {
			switch (this.stateB) {
				case INGAME:
					if (action instanceof UseItem useItemAction
							&& useItemAction.getItem().getEffect().getType() == Effect.EffectType.SWITCH) {
						applyAction(action, TeamLabel.TEAM_B);
					} else {
						this.actionB = action;
						setState(BattleState.WAITING, TeamLabel.TEAM_B);
					}
					break;

				case SWAPPING:
					this.actionB = action;
					if (action instanceof Swap){
						if (applyAction(action, TeamLabel.TEAM_B)){
							setState(BattleState.INGAME, TeamLabel.TEAM_A);
							setState(BattleState.INGAME, TeamLabel.TEAM_B);
						}
					}
				default:
					break;
			}
		}

		if (this.stateA == BattleState.WAITING && this.stateB == BattleState.WAITING){
			handleRound();
		}

	}

	private void handleRound(){
		Action currentAction = this.actionA;
		TeamLabel firstPlayer = this.checkInitiave();
		if (firstPlayer == TeamLabel.TEAM_B){
			currentAction = this.actionB;
		}
		this.applyAction(currentAction, firstPlayer);

		if (handleActionFinished(firstPlayer)){
			tickActiveEffects();
			if (gameFinished){
				handleBattleEnd();
			}
			return;
		}

		TeamLabel secondPlayer = TeamLabel.TEAM_A;
		currentAction = this.actionA;
		if (firstPlayer == TeamLabel.TEAM_A){
			secondPlayer = TeamLabel.TEAM_B;
			currentAction = this.actionB;
		}

		this.applyAction(currentAction, secondPlayer);
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
	 * @param previousActiveTeam
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

	public int computeTotalXP(Team losers){
		int boss_multiplicator = 1;
		if (isBossBattle){
			boss_multiplicator = 2;
		}
		return XP_COEF * floorNumber * boss_multiplicator * losers.size();
	}

	public Vector<Bugemon> getAvailableBugemons(boolean isTeamA){
		Team team = teamA;
		if (!isTeamA){
			team = teamB;
		}
		Vector<Bugemon> availablBugemons = new Vector<>();

		for (Bugemon b : team.getMembers()){
			if (!b.isKO()){
				availablBugemons.add(b);
			}
		}
		return availablBugemons;
	}

 

	public boolean isGameFinished(){
		return this.gameFinished;
	}

	private void resetAllFightStats(){
		for (Bugemon b : this.teamA.getMembers()){
			b.removeStatsDebuffs();
		}

		for (Bugemon b : this.teamB.getMembers()){
			b.removeStatsDebuffs();
		}
	}

	public boolean checkSwappebleBugemon(Bugemon bugemon, TeamLabel team){
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

	public List<String> getLogMsg() {
		return logMsg;
	}

	public void clearLogMsg() {
		logMsg.clear();
	}
}
