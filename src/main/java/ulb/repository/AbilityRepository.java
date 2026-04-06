package ulb.repository;

import java.util.NoSuchElementException;

import ulb.model.ability.Ability;

/**
 * Repository of the Ability instances in the game data.
 */
public interface AbilityRepository {
	/**
	 * Gets an Ability instance for the corresponding id.
	 *
	 * @param id The id of the ability
	 * @return The Ability instance
	 * @throws NoSuchElementException If no match was found for the id
	 */
	public Ability findById(String id) throws NoSuchElementException;

	/**
	 * Gives an iterable list of the abilities in the repository.
	 *
	 * @return An object iterable over the existing abilities
	 */
	public Iterable<Ability> findAll();
}
