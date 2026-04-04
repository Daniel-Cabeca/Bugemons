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
}
