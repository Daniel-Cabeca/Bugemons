package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

/**
 * Contract for executable battle actions.
 */
public abstract class Action {
	protected int priority;

	/**
	 * Executes the action for a participant.
	 *
	 * @param battle Current battle
	 * @param team Acting participant label
	 * @return True if action was executed
	 */
	public boolean executeAction(Battle battle, ParticipantLabel team) {
		return false;
	}

	/**
	 * Check if the action has a higher priority than the other action.
	 *
	 * @param otherAction the action to compare the priority with
	 * @return 1 if the priority is higher
	 * -1 if it is lower
	 * 0 if both actions have the same priority
	 */
	public int hasHightPriority(Action otherAction) {
		if (this.getPriority() == otherAction.getPriority()) {
			return 0;
		}
		return this.getPriority() > otherAction.getPriority() ? 1 : -1;
	}

	/**
	 * Get the priority of the action. Some actions have a higher priority than others.
	 * A higher priority means that the actions should be executed before.
	 *
	 * @return the priority of the action.
	 */
	public int getPriority() {
		return this.priority;
	}
}
