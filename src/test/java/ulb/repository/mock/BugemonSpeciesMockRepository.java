package ulb.repository.mock;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;

import java.util.NoSuchElementException;

import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.json.BugemonSpeciesJsonRepository;
import ulb.repository.AbilityRepository;


/**
 * Mock repository for abilities.
 * Acts as an actual repository but is actually just using a static instance of a true repository loaded only once, for performance reasons.
 */
public class BugemonSpeciesMockRepository implements BugemonSpeciesRepository {
	private static BugemonSpeciesRepository speciesRepository = null;
	private static BugemonSpeciesRepository mockData = null;

	public BugemonSpeciesMockRepository() {
		if (speciesRepository == null || mockData == null) {
			load();
		}
	}

	private static void load() {
		try {
			AbilityRepository abilityRepository = new AbilityMockRepository();

			speciesRepository = new BugemonSpeciesJsonRepository(abilityRepository);
			mockData = new BugemonSpeciesJsonRepository(MockResources.getStream(MockResources.PATH_BUGEMON_SPECIES), abilityRepository);
		} catch (LoadException e) {
			throw new IllegalStateException("Failed to load Bugemon species mock data : " + e.getMessage(), e);
		}
	}

	public static void reload() {
		load();
	}

	@Override
	public BugemonSpecies findById(String id) throws LoadException, EntityNotFoundException {
		try {
			return speciesRepository.findById(id);
		} catch (EntityNotFoundException e) {
			return mockData.findById(id);
		}
	}

	@Override
	public Iterable<BugemonSpecies> findAll() throws LoadException, EntityNotFoundException {
		return speciesRepository.findAll();
	}
}
