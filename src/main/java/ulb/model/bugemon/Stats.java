package ulb.model.bugemon;


public class Stats {
	private int hp;
	private int attack;
	private int defense;
	private int initiative;

	public Stats() {
		this.hp = 0;
		this.attack = 0;
		this.defense = 0;
		this.initiative = 0;
	}

	public Stats(int hp, int attack, int defense, int initiative) {
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.initiative = initiative;
	}

	public Stats(Stats other) {
		this.hp = other.hp;
		this.attack = other.attack;
		this.defense = other.defense;
		this.initiative = other.initiative;
	}

	/**
	 * Changes each stat by adding a (positive or negative) value
	 *
	 * @param delta the value to add to each stat
	 */
	public void change(Stats delta) {
		this.hp = (this.hp <= Math.abs(delta.hp) && delta.hp < 0) ? 0 : this.hp + delta.hp;
		this.attack = (this.attack <= Math.abs(delta.attack) && delta.attack < 0) ? 0 : this.attack + delta.attack;
		this.defense = (this.defense <= Math.abs(delta.defense) && delta.defense < 0) ? 0 :
				this.defense + delta.defense;
		this.initiative = (this.initiative <= Math.abs(delta.initiative) && delta.initiative < 0) ? 0 :
				this.initiative + delta.initiative;
	}

	/**
	 * Adds a value to stats
	 *
	 * @param delta the value to add to each stat
	 */
	public void plus(Stats delta) {
		this.hp += delta.hp;
		this.attack += delta.attack;
		this.defense += delta.defense;
		this.initiative += delta.initiative;
	}

	/**
	 * Raises all the stats to a given minimum.
	 *
	 * @param min The minimum stats
	 */
	public void setMin(Stats min) {
		if (this.hp < min.hp) {
			this.hp = min.hp;
		}
		if (this.attack < min.attack) {
			this.attack = min.attack;
		}
		if (this.defense < min.defense) {
			this.defense = min.defense;
		}
		if (this.initiative < min.initiative) {
			this.initiative = min.initiative;
		}
	}

	public int getHp() { return hp; }

	public void setHp(int hp) { this.hp = hp; }

	public int getAttack() { return attack; }

	public void setAttack(int attack) { this.attack = attack; }

	public int getDefense() { return defense; }

	public void setDefense(int defense) { this.defense = defense; }

	public int getInitiative() { return initiative; }

	public void setInitiative(int initiative) { this.initiative = initiative; }

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (!(o instanceof Stats)) {
			return false;
		}

		Stats b = (Stats) o;

		if (this.hp != b.hp || this.attack != b.attack || this.defense != b.defense || this.initiative != b.initiative) {
			return false;
		}

		return true;
	}
}
