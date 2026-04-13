package ulb.model.action;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

/**
 * Action representing the use of an ability.
 */
public class UseAbility implements Action {
    private Ability ability;

    /** Creates an empty ability action. */
    public UseAbility() {}

    /**
     * Creates an ability action.
     *
     * @param ability Ability to use
     */
    public UseAbility(Ability ability) {
        this.ability = ability;
    }

    /** Returns the selected ability. */
    public Ability getAbility() {
        return ability;
    }

    /**
     * Sets the ability to use.
     *
     * @param ability Ability to use
     */
    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    /** {@inheritDoc} */
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
