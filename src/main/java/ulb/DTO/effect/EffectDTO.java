package ulb.DTO.effect;

import ulb.model.effect.EffectType;

import java.io.Serializable;

import ulb.model.effect.EffectTarget;

/**
 * Transferable Effect, used on the vue side.
 */
public class EffectDTO implements Serializable{
	private final EffectType type;
	private final EffectTarget target;

	public EffectDTO(EffectType type, EffectTarget target){
		this.type = type;
		this.target = target;
	}

	public EffectType getType() {return type;}
	public EffectTarget getTarget() {return target;}
}