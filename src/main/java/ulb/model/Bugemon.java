package ulb.model;
import ulb.utils.Stats;

import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

public class Bugemon {
	private final String name;
	private final Type type;

	private Stats baseStats;
	private Stats fightStats;

	private int xp;
	private int level;

	private final AbilitySet moveset = new AbilitySet();

	public Bugemon(String name, Type type, int pv, int attack, int defense, int initiative, int level) {
		this.name = name;
		this.type = type;
		this.baseStats = new Stats(pv, defense, attack, initiative);
		this.fightStats = new Stats(pv, defense, attack, initiative);
		this.level = level;
		this.xp = 0;
	}

	public void increaseBaseStats(Stats delta) {
		this.baseStats.add(delta);
	}

	public void changeFightStats(Stats delta) {
		this.fightStats.add(delta);
	}

	public void gainXP(int experience) {
		this.xp += experience;

		while (this.xp >= (50 + 50 * (this.level - 1))) {
			this.xp -= (50 + 50 * (this.level - 1));
			this.level++;
		}
	}

	public final String getName() {return this.name;}
	public final Type getType() {return this.type;}
	public Stats getFighStats() {return this.fightStats;}
	public Stats getBaseStats() {return this.baseStats;}
	public int getLevel() {return this.level;}
	public int getXP() {return this.xp;}
	public AbilitySet getMoveset() {return this.moveset;}
}
