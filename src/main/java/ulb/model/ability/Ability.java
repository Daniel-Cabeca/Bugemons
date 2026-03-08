package ulb.model.ability;

import ulb.model.HasId;
import ulb.model.type.Type;
import ulb.model.Effect;
import ulb.model.bugemon.Bugemon;

/**
 * Represents a move that can be used by a Bugemon in battle.
 */
public class Ability implements HasId {
	private String id;
	private String name;
	private Type type;
	private String description;
	private int power;
	private Effect effect;

	public Ability(String id, String name, Type type, String description, int power) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.power = power;
		this.effect = null;
	}

	public Ability(String id, String name, Type type, String description, int power, Effect effect) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.power = power;
		this.effect = effect;
	}

	@Override
	public String getId() { return this.id; }

	public String getName() { return this.name; }
	public Type getType() { return this.type; }
	public String getDescription() { return this.description; }
	public int getPower() { return this.power; }
	public Effect getEffect() { return this.effect; }

	public void applyEffect(Bugemon target) {
		if (effect != null){
			this.effect.apply(target);
		}
	}

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
}
