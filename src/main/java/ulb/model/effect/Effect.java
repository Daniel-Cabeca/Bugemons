package ulb.model.effect;

import java.util.List;
import java.util.ArrayList;

import ulb.model.bugemon.Bugemon;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

/**
 * Base abstraction for effects applied during battle.
 */
public abstract class Effect {
	private final EffectTarget target;


	/**
	 * Creates an effect targeting the provided scope.
	 *
	 * @param targetType Target scope for this effect
	 */
	public Effect(EffectTarget targetType) {
		this.target = targetType;
	}

	// Getters

	/**
	 * Returns the target scope of this effect.
	 *
	 * @return Effect target scope
	 */
	public EffectTarget getTarget() { return this.target; }

	/**
	 * Applies this effect to battle entities.
	 *
	 * @param battle Current battle
	 * @param team Acting participant label
	 */
	public abstract void apply(Battle battle, ParticipantLabel team);

	/**
	 * Resolves concrete bugemon targets according to effect target scope.
	 *
	 * @param battle Current battle
	 * @param team Acting participant label
	 * @return List of targeted bugemons
	 */
	public List<Bugemon> getTargets(Battle battle, ParticipantLabel team) {
		List<Bugemon> targets = new ArrayList<>();

		switch(this.target) {
			case OWN_BUGEMON:
				targets.add(battle.getActiveBugemon(team));
				break;
			case OPPOSITE_BUGEMON:
				targets.add(battle.getActiveBugemon(battle.getOpponentTeamLabel(team)));
				break;
			case OWN_TEAM:
				targets = battle.getTeam(team).getMembers();
				break;
			default:
				break;
		}

		return targets;
	}
}