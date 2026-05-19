package ulb.repository.mock;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;

import ulb.model.ability.Ability;
import ulb.repository.AbilityRepository;
import ulb.repository.json.AbilityJsonRepository;

/**
 * Mock repository for abilities.
 * Acts as an actual repository but is actually just using a static instance of a true repository loaded only once,
 * for performance reasons.
 */
public class AbilityMockRepository implements AbilityRepository {
	private static AbilityRepository abilityRepository = null;
	private static AbilityRepository mockData = null;

	public AbilityMockRepository() {
		if (abilityRepository == null || mockData == null) {
			load();
		}
	}

	private static void load() {
		try {
			abilityRepository = new AbilityJsonRepository();
			mockData = new AbilityJsonRepository(MockResources.getStream(MockResources.PATH_ABILITIES));
		} catch (LoadException e) {
			throw new IllegalStateException("Failed to load ability mock data.");
		}
	}

	public static void reload() {
		load();
	}

	@Override
	public Ability findById(String id) throws LoadException, EntityNotFoundException {
		try {
			return abilityRepository.findById(id);
		} catch (EntityNotFoundException e) {
			return mockData.findById(id);
		}
	}

	@Override
	public Iterable<Ability> findAll() throws LoadException, EntityNotFoundException {
		return abilityRepository.findAll();
	}
}
