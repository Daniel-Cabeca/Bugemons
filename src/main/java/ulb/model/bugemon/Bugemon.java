package ulb.model.bugemon;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.type.Type;

import java.util.Random;

public class Bugemon {
	private int id;
	private final BugemonSpecies species;
	private final AbilitySet abilities;
	private Stats baseStats;
	private Stats fightStats;
	private int xp;
	private int level;
	private int remainingRewards = 0;

	public Bugemon(BugemonSpecies species) {
		this.species = species;
		this.abilities = species.getAbilities();
		this.baseStats = new Stats(this.species.getBaseStats());
		this.fightStats = new Stats(this.species.getBaseStats());
		this.xp = 0;
		this.level = 1;
	}

	public void setRemainingRewards(int remainingRewards) { this.remainingRewards = remainingRewards; }

	public AbilitySet getAbilities() { return this.abilities; }

	public int getXp() { return this.xp; }

	public void setXp(int xp) { this.xp = xp; }

	public int getLevel() { return this.level; }

	public void setLevel(int level) { this.level = level; }

	public int getRemainingReward() { return this.remainingRewards; }

	public int getId() { return this.id; }

	public void setId(int id) { this.id = id; }

	public int getAttack() { return this.getFightStats().getAttack(); }

	public Stats getFightStats() { return this.fightStats; }

	public void setFightStats(Stats fightStats) { this.fightStats = fightStats; }

	public int getDefense() { return this.getFightStats().getDefense(); }

	public boolean isKO() { return this.fightStats.getHp() <= 0; }

	public void swapAbility(Ability newAbility, Ability oldAbility) {
		if (newAbility.getType() == this.getType()) {
			abilities.swapAbility(newAbility, oldAbility);
		}
	}

	public Type getType() { return this.getSpecies().getType(); }

	public BugemonSpecies getSpecies() { return this.species; }

	public boolean hasHPDecreased() {
		return this.baseStats.getHp() > this.fightStats.getHp();
	}

	/**
	 * Resets fight stats to get rid of debuffs. If fight stat > base stat,
	 * assumes that there is no debuff and keeps the stat
	 */
	public void removeStatsDebuffs() {
		this.getFightStats().setMin(this.getBaseStats());
	}

	public Stats getBaseStats() { return this.baseStats; }

	public void setBaseStats(Stats baseStats) { this.baseStats = baseStats; }

	public int gainXp(int experience) {
		this.xp += experience;
		int gainLevels = 0;

		while (this.xp >= (50 + 50 * (this.level - 1))) {
			this.xp -= (50 + 50 * (this.level - 1));
			this.level++;
			gainLevels++;
			this.remainingRewards++;
		}
		return gainLevels; // TO REMOVE : possibilité d'appeler directement gainLevelsReward
	}

	/**
	 * Applies one pending level-up reward to this Bugémon.
	 *
	 * The reward is applied both to base stats and current fight stats so the
	 * Bugémon remains consistent after the battle that granted the level.
	 *
	 * @param reward The reward applied
	 * @return {@code true} if a pending reward was consumed, {@code false} otherwise
	 */
	public boolean consumeLevelReward(Stats reward) {
		if (this.remainingRewards <= 0) {
			return false;
		}

		changeBaseStats(reward);
		changeFightStats(reward);
		this.remainingRewards--;
		return true;
	}

	public void changeBaseStats(Stats delta) {
		this.getBaseStats().change(delta);
	}

	public void changeFightStats(Stats delta) {
		this.getFightStats().change(delta);
		if (this.getHp() > this.baseStats.getHp()) {
			this.fightStats.setHp(this.baseStats.getHp());
		}
	}

	public int getHp() { return this.getFightStats().getHp(); }

	public boolean equals(Bugemon other) {
		return this.getName() == other.getName() && this.getSpeciesId() == other.getSpeciesId() && this.getFightStats().equals(other.getFightStats()) && this.getBaseStats().equals(other.getBaseStats());
	}

	public String getName() { return this.getSpecies().getName(); }

	public String getSpeciesId() { return this.getSpecies().getId(); }

	/**
	 * Check if this bugemon have the initiative higher than the other one
	 *
	 * @param other the other bugemon to check the initiative with
	 * @return a boolean depending if this bugemon have a higher initiative, or a random boolean if both are equals
	 */
	public boolean checkInitiative(Bugemon other) {
		if (this.getInitiative() != other.getInitiative()) {
			return this.getInitiative() > other.getInitiative();
		}
		Random rand = new Random();
		return rand.nextInt(2) == 0;
	}

	public int getInitiative() { return this.getFightStats().getInitiative(); }


}
