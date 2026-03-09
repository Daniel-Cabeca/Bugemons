package ulb.model.bugemon;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

import java.util.NoSuchElementException;

/**
 * Lists and gives access to all loaded Bugemon species.
 * Uses a singleton pattern.
 *
 * @deprecated Should use services instead.
 */
@Deprecated
public class BugemonDatabase implements Iterable<BugemonSpecies> {
	private static final BugemonDatabase INSTANCE = new BugemonDatabase();
	private final Map<String, BugemonSpecies> species = new HashMap<>();

	private BugemonDatabase() {
	}

	public static BugemonDatabase getInstance() {
		return INSTANCE;
	}

	@Override
	public Iterator<BugemonSpecies> iterator() {
		return this.species.values().iterator();
	}

	/**
	 * Adds a species to the database.
	 *
	 * @param species The new species to add
	 * @throws IllegalArgumentException If the species already exists
	 */
	public void add(BugemonSpecies species) throws IllegalArgumentException {
		BugemonSpecies old = this.species.putIfAbsent(species.getId(), species);

		if (old != null) {
			throw new IllegalArgumentException("Trying to add a species that is already registered.");
		}
	}

	/**
	 * Gets a species specified by its id, if it exists.
	 *
	 * @param id The id of the species
	 * @return The species
	 * @throws NoSuchElementException If the species does not exist
	 */
	public BugemonSpecies get(String id) throws NoSuchElementException {
		BugemonSpecies species = this.species.get(id);

		if (species == null) {
			throw new NoSuchElementException("No species matches the given id.");
		}

		return species;
	}

	/**
	 * Whether an id corresponds to a loaded species.
	 *
	 * @param id The id of the species
	 * @return True if the id matches a species, false otherwise
	 */
	public boolean exists(String id) {
		return this.species.containsKey(id);
	}
}
