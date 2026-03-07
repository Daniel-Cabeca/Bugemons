package ulb.repository.loader;

import java.lang.Iterable;

import ulb.model.ability.Ability;

/**
 * Loads a list of Ability instances for the game data.
 */
public interface AbilityLoader {
	/**
	 * Loads all abilities in the game data.
	 *
	 * @return An iterable object with all the loaded abilities
	 * @throws LoadFailureException If a runtime error occurs during the loading
	 */
	public Iterable<Ability> loadAll() throws LoadFailureException;
}
