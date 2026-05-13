package ulb.DTO.bugemon;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.stats.StatsDTO;
import ulb.model.type.Type;

/**
 * Transferable Bugemon, used on the vue side.
 */
public record BugemonDTO(BugemonSpeciesDTO species, StatsDTO baseStats, StatsDTO fightStats, int xp, int level, int remainingRewards) implements Serializable {

	public BugemonDTO(BugemonSpeciesDTO species){
		this(species, species.baseStats(), species.baseStats(), 0, 1, 0);
	}

	public String getId(){return this.species().id();}
	public String getName(){return this.species().name();}
	public String getSpritePath() {return this.species().getSpritePath();}
	public Type getType(){return this.species().type();}
	public List<AbilityDTO> getAbilities(){return this.species().abilities();}

	public int getHp(){return this.fightStats().hp();}
	public int getAttack(){return this.fightStats().attack();}
	public int getDefense(){return this.fightStats().defense();}
	public int getInitiative(){return this.fightStats().initiative();}

	public Optional<AbilityDTO> findActiveAbilityById(String abilityId) {
        for (AbilityDTO ability : this.getAbilities()) {
            if (ability != null && abilityId.equals(ability.id())) {
                return Optional.of(ability);
            }
        }
        return Optional.empty();
    }

}
