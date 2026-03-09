package ulb.repository.mock;

import java.util.NoSuchElementException;
import java.io.InputStream;

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

	public BugemonSpeciesMockRepository() {
		if (speciesRepository == null) {
			load();
		}
	}

	private static void load() {
		AbilityRepository abilityRepository = new AbilityMockRepository();
		speciesRepository = new BugemonSpeciesJsonRepository(abilityRepository);
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
