package ulb.mapper.effect;

import ulb.DTO.effect.EffectDTO;
import ulb.DTO.effect.EffectHealDTO;
import ulb.DTO.effect.EffectStatModifierDTO;
import ulb.exceptions.MappingException;
import ulb.model.battle.Battle;
import ulb.model.effect.*;

/**
 * Used to convert Regular Effect to DTO Effect.
 */
public class EffectMapper {

	private EffectMapper() {}

	/**
	 * Converts an EffectDTO to an entity.
	 *
	 * @param dto The Effect DTO
	 * @return The corresponding entity or null
	 * @throws MappingException If mapping fails
	 */
	public static Effect toEntity(EffectDTO dto) throws MappingException {
		if (dto == null) return null;

		if (dto.getType() == null) {
			throw new MappingException("Effect DTO has no type.");
		}

		switch (dto.getType()) {
			case RESET_MALUS:
				return new EffectResetMalus(dto.getTarget()) {
					@Override
					public void apply(Battle battle, Battle.ParticipantLabel team) {}
				};

			case STAT_MODIFIER:
				if (dto instanceof EffectStatModifierDTO effectStat) {
					return toEntity(effectStat);
				}
				throw new MappingException("STAT_MODIFIER effect has invalid DTO type: " + dto.getClass().getSimpleName());

			case HEAL:
				if (dto instanceof EffectHealDTO effectHeal) {
					return toEntity(effectHeal);
				}
				throw new MappingException("HEAL effect has invalid DTO type: " + dto.getClass().getSimpleName());

			default:
				throw new MappingException("Unknown effect type: " + dto.getType());
		}
	}

	/**
	 * Converts an EffectStatModifierDTO to an entity.
	 *
	 * @param dto The EffectStatModifier DTO
	 * @return The corresponding entity or null
	 */
	public static EffectStatModifier toEntity(EffectStatModifierDTO dto) {
		if (dto == null) {
			return null;
		}
		return new EffectStatModifier(dto.getTarget(), dto.getDuration(), dto.getModifiers()) {};
	}

	/**
	 * Converts an EffectHealDTO to an entity.
	 *
	 * @param dto The EffectHeal DTO
	 * @return The corresponding entity or null
	 */
	public static EffectHeal toEntity(EffectHealDTO dto) {
		if (dto == null) return null;

		return new EffectHeal(dto.getTarget(), dto.getValue()) {};
	}

	/**
	 * Converts an Effect entity to a DTO.
	 *
	 * @param entity The Effect entity
	 * @return The corresponding DTO or null
	 */
	public static EffectDTO toDTO(Effect entity) {
		if (entity == null) return null;

		if (entity instanceof EffectStatModifier statModifier) {
			return new EffectStatModifierDTO(EffectType.STAT_MODIFIER, statModifier.getTarget(),
					statModifier.getDuration(), statModifier.getModifiers());
		} else if (entity instanceof EffectHeal heal) {
			return new EffectHealDTO(EffectType.HEAL, heal.getTarget(), heal.getValue());
		} else if (entity instanceof EffectResetMalus resetMalus) {
			return new EffectDTO(EffectType.RESET_MALUS, resetMalus.getTarget());
		}
		return null;
	}
}
