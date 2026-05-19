package ulb.service;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.BugemonSpeciesRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
	 * Creates a Bugemon instance from a random species, at level 1.
	 *
	 * @return The instanciated Bugemon
	 */
	public Bugemon spawnBugemonRandom() throws LoadException, EntityNotFoundException {
		Random random = new Random();
		List<String> ids = this.getAllSpeciesIds();

		String speciesId = ids.get(random.nextInt(ids.size()));
		return this.spawnBugemon(speciesId);
	}

	/**
	 * Creates a list of all species ids.
	 *
	 * @return The list of all species ids
	 */
	private List<String> getAllSpeciesIds() throws LoadException, EntityNotFoundException {
		List<String> ids = new ArrayList<>();

		for (BugemonSpecies species : this.speciesRepository.findAll()) {
			ids.add(species.getId());
		}

		return ids;
	}

	/**
	 * Creates a Bugemon instance of the specified species, at level 1.
	 *
	 * @param speciesId The id of the species
	 * @return The instanciated Bugemon
	 * @throws EntityNotFoundException If the species id was not recognized
	 */
	public Bugemon spawnBugemon(String speciesId) throws LoadException, EntityNotFoundException {
		BugemonSpecies species = this.speciesRepository.findById(speciesId);
		return new Bugemon(species);
	}

	/**
	 * Creates multiple Bugemon instances from random and distinct species, at level 1.
	 *
	 * @param number The number of Bugemons to spawn
	 * @return The spawned Bugemons
	 */
	public List<Bugemon> spawnBugemonRandomDistinct(int number) throws LoadException, EntityNotFoundException {
		List<String> ids = this.getAllSpeciesIds();
		Collections.shuffle(ids);

		List<Bugemon> spawned = new ArrayList<>();

		for (int i = 0; i < number; ++i) {
			String id = ids.get(i);
			Bugemon bugemon = this.spawnBugemon(id);
			spawned.add(bugemon);
		}

		return spawned;
	}

	/**
	 * Fetches a Bugemon species by its id.
	 *
	 * @param id The species' id
	 * @throws EntityNotFoundException If no species matches the id
	 */
	public BugemonSpecies getBugemonSpecies(String id) throws LoadException, EntityNotFoundException {
		return this.speciesRepository.findById(id);
	}

	/**
	 * Returns the list of all loaded Bugemon species.
	 *
	 * @return An iterable of all the species
	 */
	public Iterable<BugemonSpecies> getAllSpecies() throws LoadException, EntityNotFoundException {
		return this.speciesRepository.findAll();
	}
}
