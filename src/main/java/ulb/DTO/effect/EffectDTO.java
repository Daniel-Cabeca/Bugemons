package ulb.DTO.effect;

import ulb.model.effect.EffectType;

import java.io.Serializable;

import ulb.model.effect.EffectTarget;

public class EffectDTO implements Serializable{
	private EffectType type;
	private EffectTarget target;

	public EffectDTO(EffectType type, EffectTarget target){
		this.type = type;
		this.target = target;
	}

	public EffectType getType() {return type;}
	public EffectTarget getTarget() {return target;}

	public void setType(EffectType type) {this.type = type;}
	public void setTarget(EffectTarget target) {this.target = target;}
}