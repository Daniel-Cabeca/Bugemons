package ulb.model.action;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

public class UseAbility implements Action {
    private Ability ability;

    public UseAbility() {}

    public UseAbility(Ability ability) {
        this.ability = ability;
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    @Override
    public boolean executeAction(Battle battle, ParticipantLabel team) {
        if (this.ability == null) {
            return false;
        }

        Bugemon oppositeBugemon = battle.getActiveBugemon(battle.getOpponentTeamLabel(team));
        if (!oppositeBugemon.isKO()) {
            this.ability.use(battle, team);
        }

        return true;
    }
}
