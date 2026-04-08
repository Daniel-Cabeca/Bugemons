package ulb.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.type.Type;
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

    /**
     * Returns a random ability compatible with the given type and not already present in the set.
     *
     * @param type the Bugemon type
     * @param currentAbilities the abilities already known by the Bugemon
     * @return a random compatible ability, or null if none exists
     */
    public Ability getRandomAbility(Type type, AbilitySet currentAbilities) {
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
