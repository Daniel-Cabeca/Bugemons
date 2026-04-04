package ulb.model.effect;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

public class EffectSwitch extends Effect {

    public EffectSwitch(EffectTarget targetType){
        super(targetType);
    } 

    /**
     * {@inheritDoc}
     */
    @Override
    public void apply(Battle battle, ParticipantLabel team) {
        Bugemon nextBugemon = battle.getNextBugemon(team);
		battle.setActiveBugemon(nextBugemon, team);
    }

}
