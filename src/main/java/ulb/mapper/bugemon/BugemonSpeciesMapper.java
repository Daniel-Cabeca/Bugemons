package ulb.mapper.bugemon;

import java.util.List;

import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.ability.AbilityDTO;

import ulb.exceptions.MappingException;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.ability.AbilitySet;

import ulb.mapper.stats.StatsMapper;
import ulb.mapper.ability.AbilityMapper;

/**
 * Used to convert Regular Bugemon Species to DTO Bugemon Species
 */
public class BugemonSpeciesMapper {

    private BugemonSpeciesMapper(){}

    public static BugemonSpecies toEntity(BugemonSpeciesDTO dto) throws MappingException{
        if(dto == null) return null;

        Stats stats = StatsMapper.toEntity(dto.getBaseStats());
        AbilitySet abilitySet = new AbilitySet();

        List<AbilityDTO> abilities = dto.getAbilities();
        if (abilities != null) {
            for (int i = 0; i < abilities.size() && i < AbilitySet.SIZE; i++) {
                abilitySet.setAbility(i, AbilityMapper.toEntity(abilities.get(i)));
            }
        }

        return new BugemonSpecies(
                dto.getId(),
                dto.getName(),
                dto.getType(),
                stats,
                abilitySet,
                dto.getSprite(),
                dto.isStarter()
        );
    }

    public static BugemonSpeciesDTO toDTO(BugemonSpecies entity){
        if(entity == null) return null;

        BugemonSpeciesDTO dto = new BugemonSpeciesDTO();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setType(entity.getType());
        dto.setBaseStats(StatsMapper.toDTO(entity.getBaseStats()));
        dto.setSprite(entity.getSprite());
        dto.setStarter(entity.isStarter());

        if(entity.getAbilities() != null){
            for(var ability : entity.getAbilities()){
                dto.addAbility(AbilityMapper.toDTO(ability));
            }
        }

        return dto;
    }
}
