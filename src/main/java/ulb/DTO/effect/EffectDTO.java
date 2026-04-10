package ulb.DTO.effect;

public class EffectDTO {
    private EffectType type;
	private EffectTarget target;

	public enum EffectType {
		HEAL,
		STAT_MODIFIER,
		RESET_MALUS,
		SWITCH
	}

	public enum EffectTarget {
		OWN_BUGEMON,
		OPPOSITE_BUGEMON,
		OWN_TEAM
	}

	public EffectDTO(EffectType type, EffectTarget target){
		this.type = type;
		this.target = target;
	}

	public EffectType getType() {return type;}
	public EffectTarget getTarget() {return target;}

	public void setType(EffectType type) {this.type = type;}
	public void setTarget(EffectTarget target) {this.target = target;}
}
