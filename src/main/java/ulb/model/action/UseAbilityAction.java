package ulb.model.action;

import ulb.model.ability.Ability;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;

/**
 * Action representing the use of an ability.
 */
public class UseAbilityAction extends Action {
	private Ability ability;

	/**
	 * Creates an ability action.
	 *
	 * @param ability Ability to use
	 */
	public UseAbilityAction(Ability ability) {
		this();
		this.ability = ability;
	}

	/** 
	 * Creates an empty ability action. 
	 */
	public UseAbilityAction() {
		this.priority = 0;
	}

	/** 
	 * {@inheritDoc} 
	 */
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
