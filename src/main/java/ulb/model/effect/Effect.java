package ulb.model.effect;

import java.util.List;
import java.util.ArrayList;

import ulb.model.bugemon.Bugemon;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

public abstract class Effect {
	private final EffectType type;
	private final EffectTarget target;

	public enum EffectType {
		HEAL,
		STAT_MODIFIER,
		RESET_MALUS,
		SWITCH
	}

	public enum EffectTarget {
		OWN_BUGEMON,
		OPPOSITE_BUGEMON,
		OWN_TEAM
	}

	public Effect(EffectType type, EffectTarget targetType) {
		this.type = type;
		this.target = targetType;
	}

	// Getters
	public EffectType getType() { return this.type; }

	public EffectTarget getTarget() { return this.target; }

	/**
	 * Applies the effect in the given battle.
	 *
	 * @param battle The current battle
	 * @param team The team using that effect
	 */
	public abstract void apply(Battle battle, ParticipantLabel team);

	/**
	 * Returns the list of Bugemons affected by the effect.
	 *
	 * @param battle The current battle
	 * @param team The team using that effect
	 * @return The list of affected Bugemons
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
