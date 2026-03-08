package ulb.repository.mock;

import java.util.NoSuchElementException;
import java.io.InputStream;

import ulb.repository.AbilityRepository;
import ulb.repository.inmemory.AbilityInMemoryRepository;

import ulb.repository.loader.AbilityLoader;
import ulb.repository.loader.json.JsonResources;
import ulb.repository.loader.json.AbilityJsonLoader;

import ulb.model.ability.Ability;

/**
 * Mock repository for abilities.
 * Acts as an actual repository but is actually just using a static instance of a true repository loaded only once, for performance reasons.
 */
public class AbilityMockRepository implements AbilityRepository {
	private static AbilityRepository abilityRepository = null;

	public AbilityMockRepository() {
		if (abilityRepository == null) {
			load();
		}
	}

	private static void load() {
		String path = JsonResources.PATH_ABILITIES;
		InputStream stream = JsonResources.getStream(path);

		AbilityLoader loader = new AbilityJsonLoader(stream);
		Iterable<Ability> abilities = loader.loadAll();

		abilityRepository = new AbilityInMemoryRepository(abilities);
	}

	public static void reload() {
		load();
	}

	@Override
	public Ability findById(String id) throws NoSuchElementException {
		return abilityRepository.findById(id);
	}
}
