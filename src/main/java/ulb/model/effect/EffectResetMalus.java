package ulb.model.effect;

import java.util.List;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

public class EffectResetMalus extends Effect{

    public EffectResetMalus(EffectTarget targetType){
        super(EffectType.RESET_MALUS, targetType);
    }

     /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Battle battle, ParticipantLabel team) {
        List<Bugemon> targets = this.getTargets(battle, team);

		for (Bugemon target: targets) {
			target.removeStatsDebuffs();
		}
    }
}
