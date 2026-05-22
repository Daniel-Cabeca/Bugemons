package ulb.service;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.type.Type;
import ulb.repository.AbilityRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service layer for Bugemon abilities.
 */
public class AbilityService {
	/** The repository holding all existing abilities. */
	private final AbilityRepository abilityRepository;

	public AbilityService(AbilityRepository abilityRepository) {
		this.abilityRepository = abilityRepository;
	}

	/**
	 * Returns a random ability compatible with the given type and not already present in the set.
	 *
	 * @param type the Bugemon type
	 * @param currentAbilities the abilities already known by the Bugemon
	 * @return a random compatible ability, or null if none exists
	 */
	public Ability getRandomAbility(Type type, AbilitySet currentAbilities) throws LoadException,
			EntityNotFoundException {
		List<Ability> candidates = new ArrayList<>();
		for (Ability ability : this.abilityRepository.findAll()) {
			if (ability.getType() == type && (currentAbilities == null || !currentAbilities.contains(ability))) {
				candidates.add(ability);
			}
		}

		if (candidates.isEmpty()) {
			return null;
		}

		Random random = new Random();
		return candidates.get(random.nextInt(candidates.size()));
	}

}
