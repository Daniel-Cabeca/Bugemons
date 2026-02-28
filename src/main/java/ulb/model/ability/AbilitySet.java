package ulb.model.ability;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * Represents the set of abilitys usable by a Bugemon.
 */
public class AbilitySet implements Iterable<Ability> {
	private final Map<String, Ability> abilitys = new HashMap<>();

	@Override
	public Iterator<Ability> iterator() {
		return this.abilitys.values().iterator();
	}

	/**
	 * Gives the number of abilitys in the abilityset.
	 *
	 * @return The number of abilitys
	 */
	public int size() {
		return this.abilitys.size();
	}

	/**
	 * Checks whether a ability is present in the abilityset.
	 *
	 * @param id The id of the ability to check.
	 * @return True if the ability is present, false otherwise
	 */
	public boolean contains(String id) {
		return this.abilitys.containsKey(id);
	}

	/**
	 * Checks whether a ability is present in the abilityset.
	 *
	 * @param ability The ability to check.
	 * @return True if the ability is present, false otherwise
	 */
	public boolean contains(Ability ability) {
		return this.contains(ability.getId());
	}

	/**
	 * Adds a new ability to the abilityset, if not already present.
	 *
	 * @param ability The new ability
	 */
	public void add(Ability ability) {
		this.abilitys.put(ability.getId(), ability);
	}

	/**
	 * Removes a ability from the abilityset, if present.
	 *
	 * @param id The id of the ability to remove.
	 */
	public void remove(String id) {
		this.abilitys.remove(id);
	}

	/**
	 * removes a ability from the abilityset, if present.
	 *
	 * @param ability The ability to remove.
	 */
	public void remove(Ability ability) {
		this.abilitys.remove(ability.getId());
	}

	/**
	 * Swaps a known ability for a new one.
	 *
	 * @param newability The new ability
	 * @param oldability The old ability
	 * @throws IllegalArgumentException If oldability is not known.
	 */
	public void swap(Ability newability, Ability oldability) throws IllegalArgumentException {
		if (! this.contains(oldability)) {
			throw new IllegalArgumentException("Cannot swap out a ability that is not learned.");
		}

		this.remove(oldability);
		this.add(newability);
	}
}
