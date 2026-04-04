package ulb.DTO.effect;

import java.util.Map;

import ulb.model.effect.EffectStatModifier;

public class EffectStatModifierDTO extends EffectDTO {

    public Map<EffectStatModifier.StatType, Integer> modifiers;
	private EffectDuration duration;

    public enum StatType {
		HP,
		ATTACK,
		DEFENSE,
		INITIATIVE
	}

	public enum EffectDuration {
		PERMANENT,
		ROUND
	}
}
