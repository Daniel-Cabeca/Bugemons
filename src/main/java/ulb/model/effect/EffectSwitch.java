package ulb.model.effect;

import java.util.Optional;

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
        Optional<Bugemon> nextBugemon = battle.getNextBugemon(team);
		if (nextBugemon.isPresent()){
			battle.setActiveBugemon(nextBugemon.get(), team);
		}
    }

}
