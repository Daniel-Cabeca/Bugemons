package ulb.service.strategy;

import ulb.model.action.Action;
import ulb.model.battle.Battle;

public interface Strategy {
	/**
	 * Pick an action depending on the chosen Strategy
	 * Return null if no action can be picked
	 *
	 * @return The action picked
	 */
	public Action pickAction(Battle battle, Battle.ParticipantLabel teamLabel);
}
