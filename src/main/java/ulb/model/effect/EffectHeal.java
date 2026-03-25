package ulb.model.effect;

import java.util.List;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

public class EffectHeal extends Effect {

    private int value; 

    public EffectHeal(EffectTarget targetType, int value){
        super(EffectType.HEAL, targetType);
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Battle battle, ParticipantLabel team) {
        List<Bugemon> targets = this.getTargets(battle, team);

		for (Bugemon target: targets) {
            target.changeFightStats(new Stats(this.value, 0, 0, 0));
        }
    }
}
