package ulb.model.bugemon;

import ulb.model.HasId;
import ulb.model.ability.AbilitySet;
import ulb.model.type.Type;

/**
 * Holds data on a Bugemon species.
 */
public class BugemonSpecies implements HasId {
	private final String id;
	private final String name;
	private final Type type;
	private final Stats baseStats;
	private final AbilitySet abilities;
	private final String sprite;
	private final boolean starter;

	public BugemonSpecies(String id, String name, Type type, Stats baseStats, AbilitySet abilities, String sprite,
						  boolean starter) {
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

	public AbilitySet getAbilities() { return this.abilities; }

	public String getSprite() { return this.sprite; }

	public String getSpritePath() { return "/png/" + this.sprite; }

	public boolean isStarter() { return this.starter; }

	public int getHp() { return this.getBaseStats().getHp(); }

	public Stats getBaseStats() { return this.baseStats; }

	public int getAttack() { return this.getBaseStats().getAttack(); }

	public int getDefense() { return this.getBaseStats().getDefense(); }

	public int getInitiative() { return this.getBaseStats().getInitiative(); }
}
