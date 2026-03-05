package ulb.model.battle;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.type.Effectiveness;
import ulb.controller.action.*;
import ulb.model.item.Item;

import java.util.List;
import java.util.Random;
import java.util.Vector;


/**
 * View of the battle from the point of view of a player.
 */
public class BattleSnapshot {
	private Battle battle;
	private boolean isTeamA;
	private boolean hasTourEnded;

	/**
	 * Creates a battle snapshot as the point of view of either team A or team B.
	 *
	 * @param battle The battle
	 * @param isTeamA True if the battle is viewed from team A's point of view, false otherwise
	 */
	public BattleSnapshot(Battle battle, boolean isTeamA){
		this.battle = battle;
		this.isTeamA = isTeamA;
	}

	public Battle getBattle() {return this.battle;}


	/**
	 * States if the tour of the player has ended or not
	 * 
	 * @return The boolean of the state of the tour
	 */
	public boolean getTourInfo() {return this.hasTourEnded;}

	/**
	 * Gives the team that the snapshot views itself as.
	 *
	 * @return The snapshot's own team
	 */
	public Team getTeamSelf() {
		if (this.isTeamA) {
			return battle.getTeamA();
		}
		else {
			return battle.getTeamB();
		}
	}

	/**
	 * Gives the team that the snapshot views its opponent as.
	 *
	 * @return The snapshot's opponent team
	 */
	public Team getTeamOpponent() {
		if (this.isTeamA) {
			return battle.getTeamB();
		}
		else {
			return battle.getTeamA();
		}
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

	/**
	 * Gives the Bugemon that the snapshot views as its opponent's active Bugemon.
	 *
	 * @return The snapshot's active Bugemon
	 */
	public Bugemon getActiveBugemonOpponent() {
		if (this.isTeamA) {
			return battle.getActiveBugemonB();
		}
		else {
			return battle.getActiveBugemonA();
		}
	}

	/**
	 * Sets the snapshot's active Bugemon to another one.
	 *
	 * @param bugemon The Bugemon to switch in
	 */
	public void setActiveBugemonSelf(Bugemon bugemon) {
		if (this.isTeamA) {
			battle.setActiveBugemonA(bugemon);
		}
		else {
			battle.setActiveBugemonB(bugemon);
		}
	}
	/**
	 * Sets the opponent's active Bugemon to another one
	 *
	 * @param bugemon The Bugemon to switch in
	 */
	public void setActiveBugemonOpponent(Bugemon bugemon) {
		if (this.isTeamA) {
			battle.setActiveBugemonB(bugemon);
		}
		else {
			battle.setActiveBugemonA(bugemon);
		}
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
	public void useAbility(Ability ability) {
		Bugemon offensive;
		Bugemon defensive;
		if (this.isTeamA){
			offensive = this.battle.getActiveBugemonA();
			defensive = this.battle.getActiveBugemonB();
		} else {
			offensive = this.battle.getActiveBugemonB();
			defensive = this.battle.getActiveBugemonA();
		}

		int abilityDamage = computeDamage(offensive, defensive, ability);
		Stats damage = new Stats(-abilityDamage, 0, 0, 0);
		defensive.changeFightStats(damage);

	}

	/**
	 * Use the given ability against the active team A Bugemon
	 *
	 * @param ability the ability that is used
	 */
	public void useAbilityOnA(Ability ability) {
		Bugemon offensive;
		Bugemon defensive;
		if (this.isTeamA){
			offensive = this.battle.getActiveBugemonB();
			defensive = this.battle.getActiveBugemonA();
		} else {
			offensive = this.battle.getActiveBugemonA();
			defensive = this.battle.getActiveBugemonB();
		}

		int abilityDamage = computeDamage(offensive, defensive, ability);
		Stats damage = new Stats(-abilityDamage, 0, 0, 0);
		defensive.changeFightStats(damage);
	}

	public void useAction(Action action) {
		if (action instanceof UseAbility) {
			// useAbility(new Ability("1", "WaTeRPoUf", Type.AQUA, "Pouf d'eau giga mega stylé...", 10)); // exemple rando d'ability
		} else if (action instanceof Swap) {
			// appel fonction pour action SWAP
		} else if (action instanceof Run) {
			// appel fonction pour action RUN (abandon de la partie)
		} else if (action instanceof UseItem) {

			UseItem useItemAction = (UseItem) action;
			Item item = useItemAction.getItem();
			if (item.getEffect().getTarget().equals("adversaire")) {
				item.use(getActiveBugemonOpponent());
			} else {
				item.use(getActiveBugemonSelf());
			}

			// implémenter "switch" plus tard
		}

		this.hasTourEnded = true;
	}

	public Vector<Action> getAvailableActions() {
		this.hasTourEnded = false;

		Vector<Action> actions = new Vector<Action>();
		switch (this.battle.getState(this.isTeamA)) {
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
	 * Returns a random ability from the active Bugemon on the player's side.
	 *
	 * @return a randomly selected Ability of the self (Team A) active Bugemon
	 */
	public Ability getRandomAbilitySelf() {
	   AbilitySet AbilitiesA = this.getActiveBugemonSelf().getAbilities();
	   Random rand = new Random();
	   int i = rand.nextInt(AbilitiesA.SIZE);

	   return AbilitiesA.getAbility(i);
	}

	/**
	 * Returns a random ability from the active Bugemon on the opponent's side.
	 *
	 * @return a randomly selected Ability of the opponent (Team B) active Bugemon
	 */
	public Ability getRandomAbilityOpponent() {
		AbilitySet AbilitiesB = this.getActiveBugemonOpponent().getAbilities();
		Random rand = new Random();
		int i = rand.nextInt(AbilitiesB.SIZE);

		return AbilitiesB.getAbility(i);
	}

	/**
	 * Switches the active Bugemon on the player's team to a random, non-KO Bugemon.
	 */
	public void switchSelfBugemonAuto() {

		// list of non-KO bugemons
		List<Bugemon> availableBugemons = battle.getTeamA().getMembers().stream()
				.filter(b -> !b.isKO())
				.toList();

		Random rand = new Random();
		int i = rand.nextInt(availableBugemons.size());

		setActiveBugemonSelf(availableBugemons.get(i));
	}

	/**
	 * Switches the active Bugemon on the opponent's team to a random, non-KO Bugemon.
	 */
	public void switchOpponentBugemonAuto() {

		List<Bugemon> availableBugemons = battle.getTeamB().getMembers().stream()
				.filter(b -> !b.isKO())
				.toList();

		Random rand = new Random();
		int i = rand.nextInt(availableBugemons.size());

		setActiveBugemonOpponent(availableBugemons.get(i));
	}

}
