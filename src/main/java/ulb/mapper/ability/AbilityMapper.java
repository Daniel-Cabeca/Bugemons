package ulb.mapper.ability;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.effect.EffectDTO;
import ulb.exceptions.MappingException;
import ulb.mapper.effect.EffectMapper;
import ulb.model.ability.Ability;
import ulb.model.effect.EffectList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Used to convert regular ability to DTO ability.
 */
public class AbilityMapper {
	private AbilityMapper() {}

	/**
	 * Converts an ability DTO to its entity representation.
	 *
	 * @param dto The ability DTO to convert
	 * @return The corresponding ability entity, or null if the DTO is null
	 * @throws MappingException If an effect cannot be mapped
	 */
	public static Ability toEntity(AbilityDTO dto) throws MappingException {
		if (dto == null) return null;
		EffectList effectList = new EffectList();
		if (dto.effects() != null) {
			for (EffectDTO effectDTO : dto.effects()) {
				effectList.add(EffectMapper.toEntity(effectDTO));
			}
		}
		return new Ability(dto.id(), dto.name(), dto.type(), dto.description(), dto.power(), effectList);
	}

	/**
	 * Converts an ability entity to its DTO representation.
	 *
	 * @param entity The ability entity to convert
	 * @return The corresponding ability DTO, or null if the entity is null
	 */
	public static AbilityDTO toDTO(Ability entity) {
		if (entity == null) return null;
		List<EffectDTO> effects = null;
		if (entity.getEffects() != null) {
			effects = entity.getEffects().getEffects().stream().map(EffectMapper::toDTO).collect(Collectors.toList());
		}
		return new AbilityDTO(entity.getId(), entity.getName(), entity.getType(), entity.getDescription(),
				entity.getPower(), effects);
	}
}