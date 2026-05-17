package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

/**
 * Action representing a bugemon swap.
 */
public class Swap extends Action {
    private Bugemon toSwap;

    /** Creates an empty swap action. */
    public Swap() {
		this.priority = 1;
	}

    /**
     * Creates a swap action.
     *
     * @param toSwap Target bugemon to swap in
     */
    public Swap(Bugemon toSwap) {
		this();
        this.toSwap = toSwap;
    }

    /** {@inheritDoc} */
    @Override
    public boolean executeAction(Battle battle, ParticipantLabel team) {
        return battle.performSwap(this.toSwap, team);
    }
}
