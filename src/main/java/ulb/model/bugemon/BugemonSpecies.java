package ulb.model.bugemon;

import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

/**
 * Holds data on a Bugemon species.
 */
public class BugemonSpecies {
	private String id;
	private String name;
	private Type type;
	private Stats baseStats;
	private AbilitySet abilities;
	private String sprite;
	private boolean starter;

	public BugemonSpecies(String id, String name, Type type, Stats baseStats, AbilitySet abilities, String sprite, boolean starter) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.baseStats = baseStats;
		this.abilities = abilities;
		this.sprite = sprite;
		this.starter = starter;
	}

	public String getId() { return this.id; }
	public String getName() { return this.name; }
	public Type getType() { return this.type; }
	public Stats getBaseStats() { return this.baseStats; }
	public AbilitySet getAbilities() { return this.abilities; }
	public String getSprite() { return "/png/"+ this.sprite; }
	public boolean isStarter() { return this.starter; }

	public int getHp() { return this.getBaseStats().getHp(); }
	public int getAttack() { return this.getBaseStats().getHp(); }
	public int getDefense() { return this.getBaseStats().getAttack(); }
	public int getInitiative() { return this.getBaseStats().getInitiative(); }

	/**
	 * Creates a fresh Bugemon instance from this species.
	 *
	 * @return A new Bugemon instance
	 */
	public Bugemon spawn() {
		return new Bugemon(this);
	}

	// Test constructors

	public BugemonSpecies(Stats baseStats) {
		this.id = null;
		this.name = null;
		this.type = Type.PYRO;
		this.baseStats = baseStats;
		this.abilities = new AbilitySet();
		this.sprite = null;
		this.starter = false;
	}

	public BugemonSpecies(Type type, Stats baseStats) {
		this.id = null;
		this.name = null;
		this.type = type;
		this.baseStats = baseStats;
		this.abilities = new AbilitySet();
		this.sprite = null;
		this.starter = false;
	}

	public BugemonSpecies(String name, Type type, Stats baseStats) {
		this.id = null;
		this.name = name;
		this.type = type;
		this.baseStats = baseStats;
		this.abilities = new AbilitySet();
		this.sprite = null;
		this.starter = false;
	}
}
