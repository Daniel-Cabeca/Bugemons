package ulb.model.ability;

import java.util.Map;
import java.util.HashMap;

import java.util.NoSuchElementException;

/**
 * Lists and gives access to all loaded abilities.
 * Uses a singleton pattern.
 */
public class AbilityDatabase {
	private static final AbilityDatabase INSTANCE = new AbilityDatabase();
	private final Map<String, Ability> abilities = new HashMap<>();

	private AbilityDatabase() {
	}

	public static AbilityDatabase getInstance() {
		return INSTANCE;
	}

	/**
	 * Add an ability to the database.
	 *
	 * @param ability The new ability to add
	 * @throws IllegalArgumentException If the ability already exists
	 */
	public void add(Ability ability) throws IllegalArgumentException {
		Ability old = this.abilities.putIfAbsent(ability.getId(), ability);

		if (old != null) {
			throw new IllegalArgumentException("Trying to add an ability that is already registered.");
		}
	}

	/**
	 * Gets an ability specified by its id, if it exists.
	 *
	 * @param id The id of the ability
	 * @return The ability
	 * @throws NoSuchElementException If the ability does not exist
	 */
	public Ability get(String id) throws NoSuchElementException {
		Ability ability = this.abilities.get(id);

		if (ability == null) {
			throw new NoSuchElementException("No ability matches the given id.");
		}

		return ability;
	}

	/**
	 * Whether an id corresponds to a loaded ability.
	 *
	 * @param id The id of the ability
	 * @return True if the id matches an ability, false otherwise
	 */
	public boolean exists(String id) {
		return this.abilities.containsKey(id);
	}
}
