package ulb.DTO.effect;

import ulb.model.effect.EffectStatDuration;
import ulb.model.effect.EffectStatType;
import ulb.model.effect.EffectTarget;
import ulb.model.effect.EffectType;

import java.util.Map;

/**
 * Transferable Effect-Stat Modifier, used on the vue side.
 */
public class EffectStatModifierDTO extends EffectDTO {

	public final Map<EffectStatType, Integer> modifiers;
	private final EffectStatDuration duration;

	public EffectStatModifierDTO(EffectType type, EffectTarget target, EffectStatDuration duration, Map<EffectStatType
			, Integer> modifiers) {
		super(type, target);
		this.duration = duration;
		this.modifiers = modifiers;
	}

	public Map<EffectStatType, Integer> getModifiers() { return modifiers; }

	public EffectStatDuration getDuration() { return duration; }
}
