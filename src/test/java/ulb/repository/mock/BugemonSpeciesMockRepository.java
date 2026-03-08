package ulb.repository.mock;

import java.util.NoSuchElementException;
import java.io.InputStream;

import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.AbilityRepository;
import ulb.repository.inmemory.BugemonSpeciesInMemoryRepository;

import ulb.repository.loader.BugemonSpeciesLoader;
import ulb.repository.loader.json.JsonResources;
import ulb.repository.loader.json.BugemonSpeciesJsonLoader;

import ulb.model.bugemon.BugemonSpecies;

/**
 * Mock repository for abilities.
 * Acts as an actual repository but is actually just using a static instance of a true repository loaded only once, for performance reasons.
 */
public class BugemonSpeciesMockRepository implements BugemonSpeciesRepository {
	private static BugemonSpeciesRepository speciesRepository = null;

	public BugemonSpeciesMockRepository() {
		if (speciesRepository == null) {
			load();
		}
	}

	private static void load() {
		AbilityRepository abilityRepository = new AbilityMockRepository();

		String path = JsonResources.PATH_BUGEMON_SPECIES;
		InputStream stream = JsonResources.getStream(path);

		BugemonSpeciesLoader loader = new BugemonSpeciesJsonLoader(stream, abilityRepository);
		Iterable<BugemonSpecies> abilities = loader.loadAll();

		speciesRepository = new BugemonSpeciesInMemoryRepository(abilities);
	}

	public static void reload() {
		load();
	}

	@Override
	public BugemonSpecies findById(String id) throws NoSuchElementException {
		return speciesRepository.findById(id);
	}

	@Override
	public Iterable<BugemonSpecies> findAll() {
		return speciesRepository.findAll();
	}
}
