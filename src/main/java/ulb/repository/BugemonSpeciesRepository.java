package ulb.repository;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
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
	 * @throws EntityNotFoundException If no match was found for the id
	 */
	BugemonSpecies findById(String id) throws LoadException, EntityNotFoundException;

	/**
	 * Gives an iterable list of the species in the repository.
	 *
	 * @return An object iterable over the existing species
	 */
	Iterable<BugemonSpecies> findAll() throws LoadException, EntityNotFoundException;
}
