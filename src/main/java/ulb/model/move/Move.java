package ulb.model.move;

import ulb.model.type.Type;

/**
 * Represents a move that can be used by a Bugemon in battle.
 */
public class Move {
	private String id;
	private String name;
	private Type type;
	private String description;
	private int power;
	//TODO effects

	public Move(String id, String name, Type type, String description, int power) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.description = description;
		this.power = power;
		//TODO effects
	}

	public String getId() { return this.id; }
	public String getName() { return this.name; }
	public Type type() { return this.type; }
	public String description() { return this.description; }
	public int getPower() { return this.power; }
	//TODO effects

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Move)) {
			return false;
		}

		if (this.id.equals(((Move) o).id)) {
			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
}
