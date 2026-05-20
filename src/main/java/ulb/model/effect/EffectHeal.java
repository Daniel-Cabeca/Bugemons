package ulb.model.effect;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

import java.util.List;

public class EffectHeal extends Effect {

	private final int value;

	public EffectHeal(EffectTarget targetType, int value) {
		super(targetType);
		this.value = value;
	}

	public int getValue() { return this.value; }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Battle battle, ParticipantLabel team) {
		List<Bugemon> targets = this.getTargets(battle, team);
		for (Bugemon target : targets) {
			target.changeFightStats(new Stats(this.value, 0, 0, 0));
		}
	}
}
