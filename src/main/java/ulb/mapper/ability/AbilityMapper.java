package ulb.mapper.ability;

import java.util.List;
import java.util.stream.Collectors;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.effect.EffectDTO;

import ulb.model.ability.Ability;
import ulb.model.effect.EffectList;

import ulb.mapper.effect.EffectMapper;

/**
 * Used to convert Regular Ability to DTO Ability
 */
public class AbilityMapper {

    private AbilityMapper() {}

    public static Ability toEntity(AbilityDTO dto) {
        if (dto == null) return null;

        EffectList effectList = new EffectList();

        if (dto.effects() != null) {
            dto.effects().forEach(effectDTO -> {
                    effectList.add(EffectMapper.toEntity(effectDTO));
                }
            );
        }

        return new Ability(
                dto.id(),
                dto.name(),
                dto.type(),
                dto.description(),
                dto.power(),
                effectList);
    }

    public static AbilityDTO toDTO(Ability entity) {
        if (entity == null) return null;

        List<EffectDTO> effects = null;

        if (entity.getEffects() != null) {
            effects = entity.getEffects().getEffects().stream().map(EffectMapper::toDTO).collect(Collectors.toList());
        }

        return new AbilityDTO(entity.getId(),
                entity.getName(),
                entity.getType(),
                entity.getDescription(),
                entity.getPower(),
                effects);
    }
}