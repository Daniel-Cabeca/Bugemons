package ulb.service;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.NoSuchElementException;

import ulb.repository.BugemonSpeciesRepository;
import ulb.exceptions.EntityNotFoundException;
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
	 * @param speciesId The id of the species
	 * @return The instanciated Bugemon
	 * @throws EntityNotFoundException If the species id was not recognized
	 */
	public Bugemon spawnBugemon(String speciesId) throws EntityNotFoundException {
		BugemonSpecies species = this.speciesRepository.findById(speciesId);
		return new Bugemon(species);
	}

	/**
	 * Creates a list of all species ids.
	 *
	 * @return The list of all species ids
	 */
	private List<String> getAllSpeciesIds() {
		List<String> ids = new ArrayList<>();

		for (BugemonSpecies species: this.speciesRepository.findAll()) {
			ids.add(species.getId());
		}

		return ids;
	}

	/**
	 * Creates a Bugemon instance from a random species, at level 1.
	 *
	 * @return The instanciated Bugemon
	 */
	public Bugemon spawnBugemonRandom() {
		Random random = new Random();
		List<String> ids = this.getAllSpeciesIds();

		String speciesId = ids.get(random.nextInt(ids.size()));
		return this.spawnBugemon(speciesId);
	}

	/**
	 * Creates multiple Bugemon instances from random and distinct species, at level 1.
	 *
	 * @param number The number of Bugemons to spawn
	 * @return The spawned Bugemons
	 */
	public List<Bugemon> spawnBugemonRandomDistinct(int number) {
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
	public BugemonSpecies getBugemonSpecies(String id) throws EntityNotFoundException {
		return this.speciesRepository.findById(id);
	}

	/**
	 * Returns the list of all loaded Bugemon species.
	 *
	 * @return An iterable of all the species
	 */
	public Iterable<BugemonSpecies> getAllSpecies() {
		return this.speciesRepository.findAll();
	}
}
