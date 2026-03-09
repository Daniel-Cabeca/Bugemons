package ulb.model;

import java.util.Map;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

public class Effect {
	private final EffectType type;
	private final EffectTarget target;
	private final EffectDuration duration;
	private Map<StatType, Integer> modifiers;

	public enum EffectType {
		SOIN,
		STAT_MODIFIER,
		RESET_MALUS,
		SWITCH
	}

	public enum EffectTarget {
		LANCEUR,
		ADVERSAIRE,
		EQUIPE
	}

	public enum EffectDuration {
		PERMANENT,
		TOUR
	}

	public enum StatType {
		PV,
		ATTAQUE,
		DEFENSE,
		INITIATIVE
	}

	public Effect(EffectType type, EffectTarget targetType, Map<StatType, Integer> modifiers, EffectDuration duration) {
		this.type = type;
		this.target = targetType;
		this.modifiers = modifiers;
		this.duration = duration;
	}

	// Getters
	public EffectType getType() { return this.type; }

	public EffectTarget getTarget() { return this.target; }

	public EffectDuration getDuration() { return this.duration; }

	public Map<StatType, Integer> getModifiers() { return this.modifiers; }

	public int apply(Bugemon target) {
		switch (this.type) {
			case SOIN:
				target.changeFightStats(new Stats(this.modifiers.get(StatType.PV), 0, 0, 0));
				break;
			case STAT_MODIFIER:
				Stats statsChange = new Stats();
				for (Map.Entry<StatType, Integer> entry : this.modifiers.entrySet()) {
					switch (entry.getKey()) {
						case PV:
							statsChange.plus(new Stats(entry.getValue(), 0, 0, 0));
							break;
						case ATTAQUE:
							statsChange.plus(new Stats(0, entry.getValue(), 0, 0));
							break;
						case DEFENSE:
							statsChange.plus(new Stats(0, 0, entry.getValue(), 0));
							break;
						case INITIATIVE:
							statsChange.plus(new Stats(0, 0, 0, entry.getValue()));
							break;
					}
				}
				if (this.duration.equals(EffectDuration.PERMANENT)) {
					target.changeFightStats(statsChange);
				} else {
					// TODO: battle rounds and revert after one round
					target.changeFightStats(statsChange);
				}
				break;
			case RESET_MALUS:
				target.removeStatsDebuffs();
				break;
			case SWITCH:
				// The logic is in Battle (because we don't have access to the team here)
				break;
			default:
				return 0;
		}
		return 1;
	}
}
