package ulb.model.bugemon;

import java.util.NoSuchElementException;
import java.util.Random;

import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

import ulb.service.ServiceLoader;

public class Bugemon {
	private BugemonSpecies species;
	private Stats baseStats;
	private Stats fightStats;
	private int xp = 0;
	private int level = 1;
	private int remainingRewards = 0;

	public Bugemon(BugemonSpecies species) {
		this.species = species;
		this.baseStats = new Stats(this.species.getBaseStats());
		this.fightStats = new Stats(this.species.getBaseStats());
		this.xp = 0;
		this.level = 1;
	}

	public BugemonSpecies getSpecies() { return this.species; }
	public Stats getBaseStats() { return this.baseStats; }
	public Stats getFightStats() { return this.fightStats; }
	public int getXp() { return this.xp; }
	public int getLevel() { return this.level; }
	public int getRemainingReward() { return this.remainingRewards; }


	public String getId() { return this.getSpecies().getId(); }
	public String getName() { return this.getSpecies().getName(); }
	public Type getType() { return this.getSpecies().getType(); }
	public AbilitySet getAbilities() { return this.getSpecies().getAbilities(); }
	public String getSprite() { return this.getSpecies().getSprite(); }

	public int getHp() { return this.getFightStats().getHp(); }
	public int getAttack() { return this.getFightStats().getAttack(); }
	public int getDefense() { return this.getFightStats().getDefense(); }
	public int getInitiative() { return this.getFightStats().getInitiative(); }
	public boolean isKO() {return this.fightStats.hp <= 0;}

	public boolean hasHPDecreased(){
		return this.baseStats.hp > this.fightStats.hp;
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

	/**
	 * Resets fight stats to get rid of debuffs. If fight stat > base stat,
	 * assumes that there is no debuff and keeps the stat
	 */
	public void removeStatsDebuffs() {
		this.getFightStats().setMin(this.getBaseStats());
	}

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
	 * Apply a reward to Base Stats
	 * @param reward The reward applied
	 */
	public void applyLevelReward(Stats reward){
		if (this.remainingRewards > 0){
			changeBaseStats(reward);
			this.remainingRewards++;
		}
	}

	public boolean equals(Bugemon other){
		return this.getName() == other.getName() &&
			   this.getId() == other.getId() &&
			   this.getFightStats().equals(other.getFightStats()) &&
			   this.getBaseStats().equals(other.getBaseStats());
	}

	/**
	 * Check if this bugemon have the initiative higher than the other one
	 * @param other the other bugemon to check the initiative with
	 * @return a boolean depending if this bugemon have a higher initiative, or a random boolean if both are equals
	 */
	public boolean checkInitiative(Bugemon other){
		if (this.getInitiative() != other.getInitiative()){
			return this.getInitiative() > other.getInitiative();
		} 
		Random rand = new Random();
		return rand.nextInt(2) == 0;
	}

	// Deprecated constructors
	// Should be replaced by the use of services

	public Bugemon(int hp, int attack, int defense, int initiative) {
		Stats stats = new Stats(hp, attack, defense, initiative);
		BugemonSpecies species = new BugemonSpecies(stats);

		this.species = species;
		this.baseStats = new Stats(this.species.getBaseStats());
		this.fightStats = new Stats(this.species.getBaseStats());
		this.xp = 0;
		this.level = 1;
	}

	public Bugemon(Type type, int hp, int attack, int defense, int initiative) {
		Stats stats = new Stats(hp, attack, defense, initiative);
		BugemonSpecies species = new BugemonSpecies(type, stats);

		this.species = species;
		this.baseStats = new Stats(this.species.getBaseStats());
		this.fightStats = new Stats(this.species.getBaseStats());
		this.xp = 0;
		this.level = 1;
	}

	public Bugemon(String name, Type type, int hp, int attack, int defense, int initiative) {
		Stats stats = new Stats(hp, attack, defense, initiative);
		BugemonSpecies species = new BugemonSpecies(name, type, stats);

		this.species = species;
		this.baseStats = new Stats(this.species.getBaseStats());
		this.fightStats = new Stats(this.species.getBaseStats());
		this.xp = 0;
		this.level = 1;
	}

	public Bugemon(String id) throws NoSuchElementException {
		this(ServiceLoader.getBugemonService().getBugemonSpecies(id));
	}

}
