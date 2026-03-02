package ulb.model;

import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

public class Bugemon {
	private final String name;
	private final String sprite;
	private final Type type;

	private Stats baseStats;
	private Stats fightStats;

	private int xp;
	private int level;

	private final AbilitySet abilityset = new AbilitySet();

	public Bugemon(String name, String sprite, Type type, int hp, int attack, int defense, int initiative, int level) {
		this.name = name;
		this.sprite = sprite;
		this.type = type;
		this.baseStats = new Stats(hp, attack, defense, initiative);
		this.fightStats = new Stats(hp, attack, defense, initiative);
		this.level = level;
		this.xp = 0;
	}

	public void increaseBaseStats(Stats delta) {
		this.baseStats.add(delta);
	}

	public void changeFightStats(Stats delta) {
		this.fightStats.add(delta);
	}

	/**
	 * Resets fight stats to get rid of debuffs. If fight stat > base stat,
	 * assumes that there is no debuff and keeps the stat
	 */
	public void resetFightStats() {
		Stats currentFightStats = this.getFightStats();
		Stats base = this.getBaseStats();

		if (currentFightStats.getHp() < base.getHp()) {
			currentFightStats.setHp(base.getHp());
		}
		if (currentFightStats.getAttack() < base.getAttack()) {
			currentFightStats.setAttack(base.getAttack());
		}
		if (currentFightStats.getDefense() < base.getDefense()) {
			currentFightStats.setDefense(base.getDefense());
		}
		if (currentFightStats.getInitiative() < base.getInitiative()) {
			currentFightStats.setInitiative(base.getInitiative());
		}
	}

	public int gainXP(int experience) {
		this.xp += experience;
		int gainLevels = 0;

		while (this.xp >= (50 + 50 * (this.level - 1))) {
			this.xp -= (50 + 50 * (this.level - 1));
			this.level++;
			gainLevels++;
		}
		return gainLevels; // TO REMOVE : possibilité d'appeler directement gainLevelsReward
	}

	public Stats gainLevelsReward(int levelsGained) {
		/**
		 * Generate the rewards for all the levels gained randomly
		 * @param levelsGained the number of levels gained
		 * @return the rewards
		 */
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
		this.increaseBaseStats(reward);
		return reward;
	}

	public final String getName() {return this.name;}
	public final String getSprite(){return "/png/"+this.sprite;}
	public final Type getType() {return this.type;}
	public Stats getFightStats() {return this.fightStats;}
	public Stats getBaseStats() {return this.baseStats;}
	public int getLevel() {return this.level;}
	public int getXP() {return this.xp;}
	public boolean isKO() {return this.fightStats.hp == 0;}
	public AbilitySet getAbilityset() {return this.abilityset;}
}
