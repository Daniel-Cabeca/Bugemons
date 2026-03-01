package ulb.model.ability;

import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents the set of abilities usable by a Bugemon.
 * Is fixed to a size of 3 abilities.
 */
public class AbilitySet implements Iterable<Ability> {
	public final int SIZE = 3;

	private final Ability[] abilities = new Ability[SIZE];

	public AbilitySet() {
		this.abilities[0] = null;
		this.abilities[1] = null;
		this.abilities[2] = null;
	}

	public AbilitySet(Ability ability1, Ability ability2, Ability ability3) {
		this.abilities[0] = ability1;
		this.abilities[1] = ability2;
		this.abilities[2] = ability3;
	}

	@Override
	public Iterator<Ability> iterator() {
		return new Iterator<Ability>() {
			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < SIZE;
			}

			@Override
			public Ability next() {
				return abilities[i++];
			}
		};
	}

	/**
	 * Gives the number of abilities in the abilitieset.
	 *
	 * @return The number of abilities
	 */
	public int size() {
		return SIZE;
	}

	/**
	 * Checks whether a ability is present in the abilitieset.
	 *
	 * @param id The id of the ability to check.
	 * @return True if the ability is present, false otherwise
	 */
	public boolean contains(String id) {
		for (Ability ability: this) {
			if (ability.getId().equals(id)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks whether a ability is present in the abilitieset.
	 *
	 * @param ability The ability to check.
	 * @return True if the ability is present, false otherwise
	 */
	public boolean contains(Ability ability) {
		return this.contains(ability.getId());
	}

	/**
	 * Accesses the ability at a given index.
	 *
	 * @param index The index of the ability (starting from 0)
	 * @return The ability
	 * @throws IndexOutOfBoundsException If the index is invalid
	 */
	public Ability getAbility(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= SIZE) {
			throw new IndexOutOfBoundsException("Invalid index.");
		}

		return this.abilities[index];
	}

	/**
	 * Sets the ability at a given index.
	 *
	 * @param index The index
	 * @param ability The ability to set
	 * @throws IndexOutOfBoundsException If the index is invalid
	 */
	public void setAbility(int index, Ability ability) throws IndexOutOfBoundsException {
		if (index < 0 || index >= SIZE) {
			throw new IndexOutOfBoundsException("Invalid index.");
		}

		this.abilities[index] = ability;
	}

	/**
	 * Swaps a known ability for a new one.
	 *
	 * @param newAbility The new ability
	 * @param oldAbility The old ability
	 * @throws IllegalArgumentException If old ability is not known.
	 */
	public void swapAbility(Ability newAbility, Ability oldAbility) throws IllegalArgumentException {
		for (int i = 0; i < SIZE; ++i) {
			Ability currentAbility = this.abilities[i];

			if (currentAbility.equals(oldAbility)) {
				this.abilities[i] = newAbility;
				return;
			}
		}

		throw new IllegalArgumentException("Cannot swap out a ability that is not learned.");
	}


	public Ability getRandomAbility(){
		int min = 0;
		int max = this.abilities.length;
		int value = ThreadLocalRandom.current().nextInt(min, max + 1);
		return this.abilities[value];
	}
}
