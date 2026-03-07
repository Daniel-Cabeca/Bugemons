package ulb.repository;

import java.lang.Iterable;
import java.util.NoSuchElementException;

import ulb.model.bugemon.BugemonSpecies;
/**
 * Repository of the BugemonSpecies instances in the game data.
 */
public interface BugemonSpeciesRepository {
	/**
	 * Gets a BugemonSpecies instance for the corresponding id.
	 *
	 * @param id The id of the species
	 * @return The BugemonSpecies instance
	 * @throws NoSuchElementException If no match was found for the id
	 */
	public BugemonSpecies findById(String id) throws NoSuchElementException;

	/**
	 * Gives an iterable list of the species in the repository.
	 *
	 * @return An object iterable over the existing species
	 */
	public Iterable<BugemonSpecies> findAll();
}
