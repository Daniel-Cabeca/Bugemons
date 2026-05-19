package ulb.model.effect;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

import java.util.List;

public class EffectResetMalus extends Effect {

	public EffectResetMalus(EffectTarget targetType) {
		super(targetType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void apply(Battle battle, ParticipantLabel team) {
		List<Bugemon> targets = this.getTargets(battle, team);

		for (Bugemon target : targets) {
			target.removeStatsDebuffs();
		}
	}
}
