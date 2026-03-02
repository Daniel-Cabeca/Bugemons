package ulb.model.battle;

import ulb.model.ability.Ability;
import ulb.model.team.Team;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.type.Effectiveness;
import ulb.model.type.Type;
import ulb.utils.ActionEnum;
import java.util.Vector;


/**
 * View of the battle from the point of view of a player.
 */
public class BattleSnapshot {
	private Battle battle;
	private boolean isTeamA;

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
	 * Use the ability given against the opposing active Bugemon
	 *
	 * @param ability the ability that is used
	 */
	public void useAbility(Ability ability) {
		Bugemon offensive;
		Bugemon defensive;
		if (this.isTeamA){
			offensive = this.battle.getActiveBugemonA();
			defensive = this.battle.getActiveBugemonB();
		}else {
			offensive = this.battle.getActiveBugemonB();
			defensive = this.battle.getActiveBugemonA();
		}

		int abilityDamage = computeDamage(offensive, defensive, ability);
		Stats damage = new Stats(-abilityDamage, 0, 0, 0);
		defensive.addFightStats(damage);



	}

	public void useAction(ActionEnum action) {
		switch (action) {
			case ATTACK:
				useAbility(new Ability("1", "WaTeRPoUf", Type.AQUA, "Pouf d'eau giga mega stylé...", 10)); // exemple rando d'ability
				// prblm de spécification d'ability, résolution à venir avec nouvelle représentation d'ActionEnum
				break;

			case SWAP:
				// appel fonction pour action SWAP
				break;

			case RUN:
				// appel fonction pour action RUN (abandon de la partie)
				break;

			case USEITEM:
				// appel fonction pour action USEITEM
				break;

			default:
				break;
		}
	}

	public Vector<ActionEnum> getAvailableActions() {
		Vector<ActionEnum> actions = new Vector<ActionEnum>();
		switch (this.battle.getState(this.isTeamA)) {
			case INGAME:
				actions.add(ActionEnum.ATTACK);
				actions.add(ActionEnum.RUN);
				actions.add(ActionEnum.SWAP);
				actions.add(ActionEnum.USEITEM);
				break;

			case SWAPPING:
				actions.add(ActionEnum.SWAP);
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
}
