package ulb.model.battle;

import ulb.model.ability.Ability;
import ulb.model.team.Team;
import ulb.model.Bugemon;
import ulb.utils.Stats;
import ulb.model.type.Effectiveness;

/**
 * View of the battle from the point of view of a player.
 */
public class BattleSnapshot {
	private Battle battle;
	private boolean isTeamA;

	/**
	 * Creates a battle snapshot as the point of view of either team A or team A.
	 *
	 * @param battle The battle
	 * @param isTeamA True if the battle is viewed from team A's poiint of view, false otherwise
	 */
	BattleSnapshot(Battle battle, boolean isTeamA){
		this.battle = battle;
		this.isTeamA = isTeamA;
	}

	/**
	 * Gives the team that the snapshot views itself as.
	 *
	 * @return The snapshot's own team
	 */
	public Team getTeamSelf(){
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
	public Team getTeamOpponent(){
		if (this.isTeamA) {
			return battle.getTeamB();
		}
		else {
			return battle.getTeamA();
		}
	}
	private int computeDamage(Bugemon offensive, Bugemon defensive, int abilityPower) {
		/**
		 * Compute the damage based on the complete formula
		 *
		 * @param offensive The bugemon that uses the ability
		 * @param defensive The bugemon targeted by the ability
		 * @param abilityPower The power of the ability used
		 * @return the computed damage based on the formula
		 */
		int attackValue = offensive.getFighStats().attack;
		int defenseValue = defensive.getFighStats().defense;

		int attackFactor = (100 + attackValue) / 100;
		int defenseFactor = 100 / (100 + defenseValue);

		int damage = attackFactor * defenseFactor * abilityPower;

		Effectiveness typeFactor = new Effectiveness(offensive.getType(), defensive.getType());

		float criticalHitFactor = 1f;
		if (Math.random() <= 0.1){
			criticalHitFactor = 1.5f;
		}

		return Math.round(damage * typeFactor.getFactor() * criticalHitFactor);
	}

	public void useAbility(Ability ability) {
		/**
		 * Use the ability given against the opposing active Bugemon
		 *
		 * @param ability the ability that is used
		 */
		Bugemon offensive;
		Bugemon defensive;
		if (this.isTeamA){
			offensive = this.battle.getActiveBugemonA();
			defensive = this.battle.getActiveBugemonB();
		}else {
			offensive = this.battle.getActiveBugemonB();
			defensive = this.battle.getActiveBugemonA();
		}

		int abilityDamage = computeDamage(offensive, defensive, ability.getPower());
		Stats damage = new Stats(-abilityDamage, 0, 0, 0);
		defensive.changeFightStats(damage);

		// TODO check if bugemon is KO and if all the bugemons are KO
	}
}
