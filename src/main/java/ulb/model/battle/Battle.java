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

		int abilityDamage = computeDamage(offensive, defensive, ability);
		Stats damage = new Stats(-abilityDamage, 0, 0, 0);
		defensive.changeFightStats(damage);

	}

	private void useItem(Item item, TeamLabel team){
		if (item.getTarget().equals(Effect.EffectTarget.ADVERSAIRE)) {
			switch (team) {
				case TEAM_A:
					item.use(this.activeBugemonB);
					break;
				
				case TEAM_B:
					item.use(this.activeBugemonA);
					break;

				default:
					break;
			}
			
		} else if (item.getTarget().equals(Effect.EffectTarget.LANCEUR)) {
			switch (team) {
				case TEAM_A:
					item.use(this.activeBugemonA);
					break;
				
				case TEAM_B:
					item.use(this.activeBugemonB);
					break;

				default:
					break;
			}
		} else {
			switch (team) {
				case TEAM_A:
					for (Bugemon b : this.teamA.getMembers()){
						item.use(b);
					}
					break;
				
				case TEAM_B:
					for (Bugemon b : this.teamB.getMembers()){
						item.use(b);
					}
					break;

				default:
					break;
			}	
		}

		if (item.getEffect().getType().equals(Effect.EffectType.SWITCH)) {
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
		} else if (team == TeamLabel.TEAM_B && teamB.contains(target)){
			setActiveBugemonB(target);
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

	private void applyAction(Action action, TeamLabel team){
		if (action instanceof UseAbility useAbilityAction) {

			this.useAbility(useAbilityAction.getAbility(), team);

		} else if (action instanceof Swap swapAction) {

			this.swap(swapAction.getToSwap(), team);

		} else if (action instanceof Run) {

			this.setState(BattleState.LOST, team);

		} else if (action instanceof UseItem useItemAction) {
			this.useItem(useItemAction.getItem(), team);
		}
	}

	public synchronized void setAction(Action action, boolean isTeamA){
		if (isTeamA){
			switch (this.stateA) {
				case INGAME:
					this.actionA = action;
					setState(BattleState.WAITING, TeamLabel.TEAM_A);
					break;
				
				case SWAPPING:
					this.actionA = action;
					if (action instanceof Swap){
						applyAction(action, TeamLabel.TEAM_A);
						setState(BattleState.INGAME, TeamLabel.TEAM_A);
						setState(BattleState.INGAME, TeamLabel.TEAM_B);
					}
				default:
					break;
			}
		} else {
			switch (this.stateB) {
				case INGAME:
					this.actionB = action;
					setState(BattleState.WAITING, TeamLabel.TEAM_B);
					break;
				
				case SWAPPING:
					this.actionB = action;
					if (action instanceof Swap){
						applyAction(action, TeamLabel.TEAM_B);
						setState(BattleState.INGAME, TeamLabel.TEAM_A);
						setState(BattleState.INGAME, TeamLabel.TEAM_B);
					}
				default:
					break;
			}
		}
		
		System.out.println("STATE TEAM_A : " + this.stateA);
		System.out.println("STATE TEAM_B : " + this.stateB);

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
			if (gameFinished){
				handleBattleEnd();
			}
			return;
		}
		setState(BattleState.INGAME, TeamLabel.TEAM_A);
		setState(BattleState.INGAME, TeamLabel.TEAM_B);
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

	private int computeTotalXP(Team losers){
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
}
