package ulb.repository.mock;

import java.util.NoSuchElementException;

import ulb.model.ability.Ability;
import ulb.repository.AbilityRepository;
import ulb.repository.json.AbilityJsonRepository;

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
		abilityRepository = new AbilityJsonRepository();
	}

	public static void reload() {
		load();
	}

	@Override
	public Ability findById(String id) throws NoSuchElementException {
		return abilityRepository.findById(id);
	}
}
