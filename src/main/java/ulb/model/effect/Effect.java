package ulb.model.effect;

import java.util.List;
import java.util.ArrayList;

import ulb.model.bugemon.Bugemon;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

public abstract class Effect {
	private final EffectType type;
	private final EffectTarget target;

	public Effect(EffectType type, EffectTarget target) {
		this.type = type;
		this.target = target;
	}

	public EffectType getType() { return this.type; }
	public EffectTarget getTarget() { return this.target; }

	public abstract void apply(Battle battle, ParticipantLabel team);

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