package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

/**
 * Action representing a bugemon swap.
 */
public class SwapAction extends Action {
	private Bugemon toSwap;

	/**
	 * Creates a swap action.
	 *
	 * @param toSwap Target bugemon to swap in
	 */
	public SwapAction(Bugemon toSwap) {
		this();
		this.toSwap = toSwap;
	}

	/** 
	 * Creates an empty swap action. 
	 */
	public SwapAction() {
		this.priority = 1;
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public boolean executeAction(Battle battle, ParticipantLabel team) {
		return battle.performSwap(this.toSwap, team);
	}
}
