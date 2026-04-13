package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

/**
 * Contract for executable battle actions.
 */
public interface Action {
    /**
     * Executes the action for a participant.
     *
     * @param battle Current battle
     * @param team Acting participant label
     * @return True if action was executed
     */
    boolean executeAction(Battle battle, ParticipantLabel team);
}
