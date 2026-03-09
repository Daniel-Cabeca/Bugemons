package ulb.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.NoSuchElementException;

import ulb.repository.BugemonSpeciesRepository;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;

/**
 * Provides Bugemon-related business logic.
 */
public class BugemonService {
	private final BugemonSpeciesRepository speciesRepository;

	/**
	 * Creates a Bugemon service.
	 *
	 * @param speciesRepository The BugemonSpecies repository to pull data from and push data to
	 */
	public BugemonService(BugemonSpeciesRepository speciesRepository) {
		this.speciesRepository = speciesRepository;
	}

	/**
	 * Creates a Bugemon instance of the specified species, at level 1.
	 *
	 * @param spedies_id The id of the species
	 * @return The instanciated Bugemon
	 * @throws NoSuchElementException If the species id was not recognized
	 */
	public Bugemon spawnBugemon(String speciesId) throws NoSuchElementException {
		BugemonSpecies species = this.speciesRepository.findById(speciesId);
		return new Bugemon(species);
	}

	/**
	 * Creates a Bugemon instance from a random species, at level 1.
	 *
	 * @return The instanciated Bugemon
	 */
	public Bugemon spawnBugemonRandom() {
		Random random = new Random();
		List<String> ids = new ArrayList<>();

		for (BugemonSpecies species: this.speciesRepository.findAll()) {
			ids.add(species.getId());
		}

		String speciesId = ids.get(random.nextInt(ids.size()));
		return this.spawnBugemon(speciesId);
	}

	/**
	 * Fetches a Bugemon species by its id.
	 *
	 * @param id The species' id
	 * @throws NoSuchElementException If no species matches the id
	 */
	public BugemonSpecies getBugemonSpecies(String id) throws NoSuchElementException {
		return this.speciesRepository.findById(id);
	}

	/**
	 * Returns the list of all Bugemon species.
	 *
	 * @return An iterable of all the species
	 */
	public Iterable<BugemonSpecies> getAllSpecies() {
		return this.speciesRepository.findAll();
	}
}
