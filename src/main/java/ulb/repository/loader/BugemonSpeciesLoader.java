package ulb.repository.loader;

import java.lang.Iterable;

import ulb.model.bugemon.BugemonSpecies;

/**
 * Loads a list of BugemonSpecies instances for the game data.
 */
public interface BugemonSpeciesLoader {
	/**
	 * Loads all bugemon species in the game data.
	 *
	 * @return An iterable object with all the loaded species
	 * @throws LoadException If a runtime error occurs during the loading
	 */
	public Iterable<BugemonSpecies> loadAll() throws LoadException;
}
