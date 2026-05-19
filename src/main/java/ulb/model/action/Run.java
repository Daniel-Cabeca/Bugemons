package ulb.model.action;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

/**
 * Action representing a battle forfeit.
 */
public class Run extends Action {
	public Run() {
		this.priority = 3;
	}

	/** {@inheritDoc} */
	@Override
	public boolean executeAction(Battle battle, ParticipantLabel team) {
		battle.forfeit(team);
		return true;
	}
}
