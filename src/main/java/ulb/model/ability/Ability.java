package ulb.model.ability;

import java.lang.Math;
import java.util.Random;

import ulb.model.HasId;
import ulb.model.type.Type;
import ulb.model.effect.EffectList;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;
import ulb.model.type.Effectiveness;

/**
 * Represents a move that can be used by a Bugemon in battle.
 */
public class Ability implements HasId {
	public static final float CRITICAL_HIT_FACTOR = 1.5f;
	public static final float CRITICAL_HIT_CHANCE = 0.1f;

	private String id;
	private String name;
	private Type type;
	private String description;
	private int power;
	private EffectList effects;

	public Ability(String id, String name, Type type, String description, int power) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.power = power;
		this.effects = new EffectList();
	}

	public Ability(String id, String name, Type type, String description, int power, EffectList effects) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.power = power;
		this.effects = effects;
	}

	@Override
	public String getId() { return this.id; }

	public String getName() { return this.name; }
	public Type getType() { return this.type; }
	public String getDescription() {return this.description;}
	public int getPower() { return this.power; }
	public EffectList getEffects() { return this.effects; }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Ability)) {
			return false;
		}

		if (this.id.equals(((Ability) o).id)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	/**
	 * Uses the ability in combat.
	 *
	 * @param battle The current battle
	 * @param team The team of the Bugemon using the ability
	 * @param random The random number generator
	 */
	public void use(Battle battle, ParticipantLabel team, Random random) {
		Bugemon ownBugemon = battle.getActiveBugemon(team);
		Bugemon oppositeBugemon = battle.getActiveBugemon(battle.getOpponentTeamLabel(team));

		int damage = this.getDamage(ownBugemon, oppositeBugemon, random);
		Stats damageStats = new Stats(-damage, 0, 0, 0);
		oppositeBugemon.changeFightStats(damageStats);

		this.writeLogs(battle, ownBugemon, oppositeBugemon, damage);

		this.effects.apply(battle, team);
	}

	/**
	 * Writes the message logs from using this ability.
	 *
	 * @param battle The current battle
	 * @param ownBugemon The Bugemon using this ability
	 * @param oppositeBugemon The Bugemon this ability is used against
	 * @param damage The damage dealt
	 */
	private void writeLogs(Battle battle, Bugemon ownBugemon, Bugemon oppositeBugemon, int damage) {
		battle.addLogMsg(ownBugemon.getName() + " a utilisé " + this.getName() + ". " + oppositeBugemon.getName() + " perd " +
				damage + " PV!");

		String effectivenessMessage = getEffectivenessMessage(oppositeBugemon);
		if (!effectivenessMessage.equals("")) {
			battle.addLogMsg(effectivenessMessage);
		}
	}

	/**
	 * Gets the effectiveness message of the ability
	 *
	 * @param oppositeBugemon The Bugemon this ability is used against
	 * @return The effectiveness message (or an empty string if the effectiveness is normal)
	 */
	public String getEffectivenessMessage(Bugemon oppositeBugemon) {
		Effectiveness.Category effectivenessCategory = Effectiveness.getCategory(this.getType(), oppositeBugemon.getType());

		switch(effectivenessCategory) {
			case HIGH:
				return "Super efficace !";

			case LOW:
				return "Pas très efficace !";

			default:
				return "";
		}
	}

	/**
	 * Uses the ability in combat.
	 *
	 * @param battle The current battle
	 * @param team The team of the Bugemon using the ability
	 */
	public void use(Battle battle, ParticipantLabel team) {
		this.use(battle, team, new Random());
	}

	/**
	 * Calculate the damage done when using this ability.
	 *
	 * @param ownBugemon The Bugemon using this ability
	 * @param oppositeBugemon The Bugemon this ability is used against
	 * @param random The random number generator
	 * @return The damage dealt
	 */
	public int getDamage(Bugemon ownBugemon, Bugemon oppositeBugemon, Random random) {
		float attackValue = ownBugemon.getFightStats().getAttack();
		float defenseValue = oppositeBugemon.getFightStats().getDefense();

		float attackFactor = (100 + attackValue) / 100f;
		float defenseFactor = 100f / (100 + defenseValue);

		float baseDamage = attackFactor * defenseFactor * this.getPower();
		float typeFactor = Effectiveness.getFactor(this.getType(), oppositeBugemon.getType());

		float criticalHitFactor = 1f;
		if (random.nextDouble() <= CRITICAL_HIT_CHANCE){
			criticalHitFactor = CRITICAL_HIT_FACTOR;
		}

		return Math.round(baseDamage * typeFactor * criticalHitFactor);
	}
}
