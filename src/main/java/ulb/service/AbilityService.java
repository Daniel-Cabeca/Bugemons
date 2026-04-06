package ulb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ulb.model.ability.Ability;
import ulb.repository.AbilityRepository;

public class AbilityService {

    private final AbilityRepository abilityRepository;

    public AbilityService(AbilityRepository abilityRepository){
        this.abilityRepository = abilityRepository;
    }

    /**
	 * Returns a random ability.
	 *
	 * @return A random ability
	 */
	public Ability getRandomAbility() {
		List<Ability> abilities = new ArrayList<>();
		for (Ability ability : this.abilityRepository.findAll()) {
			abilities.add(ability);
		}
		Random random = new Random();
		return abilities.get(random.nextInt(abilities.size()));
	}
    
}
