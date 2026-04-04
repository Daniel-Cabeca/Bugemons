package ulb.DTO.effect;

import java.util.Map;

import ulb.model.effect.EffectStatModifier;
import ulb.model.effect.EffectType;
import ulb.model.effect.EffectTarget;

public class EffectStatModifierDTO extends EffectDTO {

    public Map<EffectStatModifierDTO.StatType, Integer> modifiers;
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

	public EffectStatModifierDTO(EffectType type, EffectTarget target, EffectDuration duration, Map<EffectStatModifierDTO.StatType, Integer> modifiers){
		super(type, target);
		this.duration = duration;
		this.modifiers = modifiers;
	}

	public Map<EffectStatModifierDTO.StatType, Integer> getModifiers() {return modifiers;}
	public EffectDuration getDuration() {return duration;}
	public Integer getModifier(EffectStatModifierDTO.StatType type) {
		if (this.modifiers.containsKey(type)) {
			return this.modifiers.get(type);
		}
		return -1;
	}

	public void setModifiers(Map<EffectStatModifierDTO.StatType, Integer> modifiers) {this.modifiers = modifiers;}
	public void addModifier(EffectStatModifierDTO.StatType type, Integer modifier){this.modifiers.put(type, modifier);}
	public void setDuration(EffectDuration duration) {this.duration = duration;}
}
