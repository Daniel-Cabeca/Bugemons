package ulb.service.strategy;

import ulb.model.action.Action;
import ulb.model.battle.Battle;

/**
 * Classes holding the logic for picking actions in battle.
 */
public interface Strategy {
	/**
	 * Pick an action depending on the chosen Strategy
	 * Return null if no action can be picked
	 *
	 * @return The action picked
	 */
	Action pickAction(Battle battle, Battle.ParticipantLabel teamLabel);
}
