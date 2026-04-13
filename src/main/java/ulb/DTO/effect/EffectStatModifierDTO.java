package ulb.DTO.effect;

import java.util.Map;

import ulb.model.effect.EffectStatDuration;
import ulb.model.effect.EffectStatType;
import ulb.model.effect.EffectType;
import ulb.model.effect.EffectTarget;

/**
 * Transferable Effect-Stat Modifier, used on the vue side.
 */
public class EffectStatModifierDTO extends EffectDTO {

    public Map<EffectStatType, Integer> modifiers;
	private EffectStatDuration duration;

	public EffectStatModifierDTO(EffectType type, EffectTarget target, EffectStatDuration duration, Map<EffectStatType, Integer> modifiers){
		super(type, target);
		this.duration = duration;
		this.modifiers = modifiers;
	}

	public Map<EffectStatType, Integer> getModifiers() {return modifiers;}
	public EffectStatDuration getDuration() {return duration;}
	public Integer getModifier(EffectStatType type) {
		if (this.modifiers.containsKey(type)) {
			return this.modifiers.get(type);
		}
		return -1;
	}

	public void setModifiers(Map<EffectStatType, Integer> modifiers) {this.modifiers = modifiers;}
	public void addModifier(EffectStatType type, Integer modifier){this.modifiers.put(type, modifier);}
	public void setDuration(EffectStatDuration duration) {this.duration = duration;}
}
