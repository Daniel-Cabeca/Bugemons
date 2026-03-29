package ulb.model.effect;

import java.util.List;
import java.util.Map;

import ulb.model.battle.ActiveEffect;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

public class EffectStatModifier extends Effect {

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

	public Map<EffectStatModifier.StatType, Integer> modifiers;
	private EffectDuration duration;

	public EffectStatModifier(EffectTarget targetType, EffectDuration duration, Map<StatType, Integer> modifiers){
		super(EffectType.STAT_MODIFIER, targetType);
		this.modifiers = modifiers;
		this.duration = duration;

	}

	/**
	* {@inheritDoc}
	*/
	@Override
	public void apply(Battle battle, ParticipantLabel team) {
		List<Bugemon> targets = this.getTargets(battle, team);

		for (Bugemon target: targets) {
			target.changeFightStats(buildStatsChange());
		}

		if (this.getDuration() == EffectDuration.ROUND) {
			Stats delta = this.buildStatsChange();
			for (Bugemon target : targets) {
				battle.getActiveEffects().add(new ActiveEffect(target, delta, 1, ""));
			}
		}

	}

	/**
	* Builds the Stats object used to change the Stats of a Bugemon
	*
	* @return the built Stats object
	*/
	public Stats buildStatsChange() {
		Stats statsChange = new Stats();
		for (Map.Entry<StatType, Integer> entry : this.modifiers.entrySet()) {
			switch (entry.getKey()) {
				case HP:
					statsChange.plus(new Stats(entry.getValue(), 0, 0, 0));
					break;
				case ATTACK:
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
		return statsChange;
	}

	public Map<EffectStatModifier.StatType, Integer> getModifiers() { return this.modifiers; }
	public EffectDuration getDuration() { return this.duration;}


}
