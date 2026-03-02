package ulb.model.bugemon;

import java.util.NoSuchElementException;

import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

public class Bugemon {
	private BugemonSpecies species;
	private Stats baseStats;
	private Stats fightStats;
	private int xp;
	private int level;

	public Bugemon(BugemonSpecies species) {
		this.species = species;
		this.baseStats = new Stats(this.species.getBaseStats());
		this.fightStats = new Stats(this.species.getBaseStats());
		this.xp = 0;
		this.level = 1;
	}

	/**
	 * Creates a Bugemon instance with a species given by its id.
	 * Equivalent to PokemonDatabase.getInstance().get(id).spawn();
	 *
	 * @param id The idea
	 */
	public Bugemon(String id) throws NoSuchElementException {
		this(BugemonDatabase.getInstance().get(id));
	}

	public BugemonSpecies getSpecies() { return this.species; }
	public Stats getBaseStats() { return this.baseStats; }
	public Stats getFightStats() { return this.fightStats; }
	public int getXp() { return this.xp; }
	public int getLevel() { return this.level; }

	public String getId() { return this.getSpecies().getId(); }
	public String getName() { return this.getSpecies().getName(); }
	public Type getType() { return this.getSpecies().getType(); }
	public AbilitySet getAbilities() { return this.getSpecies().getAbilities(); }
	public String getSprite() { return this.getSpecies().getSprite(); }

	public int getHp() { return this.getFightStats().getHp(); }
	public int getAttack() { return this.getFightStats().getAttack(); }
	public int getDefense() { return this.getFightStats().getDefense(); }
	public int getInitiative() { return this.getFightStats().getInitiative(); }

	public void addBaseStats(Stats delta) {
		this.getBaseStats().add(delta);
	}

	public void addFightStats(Stats delta) {
		this.getFightStats().add(delta);
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
		}
		return gainLevels; // TO REMOVE : possibilité d'appeler directement gainLevelsReward
	}

	/**
	 * Generate the rewards for all the levels gained randomly
	 * @param levelsGained the number of levels gained
	 * @return the rewards
	 */
	public Stats gainLevelsReward(int levelsGained) {
		Stats reward = new Stats();
		for (int l = 0; l < levelsGained; l++) {
			for (int i = 0; i < 10; i++) {
				int chosenStat = (int) (Math.random() * 4); // Number in [0, 3]

				switch (chosenStat) {
					case 0:
						reward.hp += 2;
						break;

					case 1:
						reward.initiative += 2;
						break;

					case 2:
						reward.attack += 1;
						break;

					case 3:
						reward.defense += 1;
						break;

					default:
						break;
				}
			}
		}
		this.addBaseStats(reward);
		return reward;
	}

	public boolean isKO() {return this.fightStats.hp <= 0;}

	// Test constructors

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
}
