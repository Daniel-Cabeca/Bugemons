package ulb.model.effect;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.battle.ActiveEffect;

public class Effect {
	private final EffectType type;
	private final EffectTarget target;
	private final EffectDuration duration;
	private Map<StatType, Integer> modifiers;

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

	public enum EffectDuration {
		PERMANENT,
		ROUND
	}

	public enum StatType {
		HP,
		ATTACK,
		DEFENSE,
		INITIATIVE
	}

	public Effect(EffectType type, EffectTarget targetType, Map<StatType, Integer> modifiers, EffectDuration duration) {
		this.type = type;
		this.target = targetType;
		this.modifiers = modifiers;
		this.duration = duration;
	}

	// Getters
	public EffectType getType() { return this.type; }

	public EffectTarget getTarget() { return this.target; }

	public EffectDuration getDuration() { return this.duration; }

	public Map<StatType, Integer> getModifiers() { return this.modifiers; }

	/**
	 * Builds the Stats object used to change the Stats of a Bugemon
	 *
	 * @return the built Stats object
	 */
	public Stats buildStatsChange() {
		Stats statsChange = new Stats();
		for (Map.Entry<StatType, Integer> entry : this.modifiers.entrySet()) {
			switch (entry.getKey()) {
				case HP:
					statsChange.plus(new Stats(entry.getValue(), 0, 0, 0));
					break;
				case ATTACK:
					statsChange.plus(new Stats(0, entry.getValue(), 0, 0));
					break;
				case DEFENSE:
					statsChange.plus(new Stats(0, 0, entry.getValue(), 0));
					break;
				case INITIATIVE:
					statsChange.plus(new Stats(0, 0, 0, entry.getValue()));
					break;
			}
		}
		return statsChange;
	}

	/**
	 * Applies the effect on a Bugemon
	 *
	 * @param target the target Bugemon
	 * @return 0 if no effect is applied, 1 if the effect is applied
	 */
	public int apply(Bugemon target) {
		switch (this.type) {
			case HEAL:
				target.changeFightStats(new Stats(this.modifiers.get(StatType.HP), 0, 0, 0));
				break;

			case STAT_MODIFIER:
				target.changeFightStats(buildStatsChange());
				break;

			case RESET_MALUS:
				target.removeStatsDebuffs();
				break;

			case SWITCH:
				// do nothing
				// TODO this whole method should be replacer with polymorphism
				break;

			default:
				return 0;
		}
		return 1;
	}

	/**
	 * Applies the effect in the given battle.
	 *
	 * @param battle The current battle
	 * @param team The team using that effect
	 */
	public void apply(Battle battle, ParticipantLabel team) {
		List<Bugemon> targets = this.getTargets(battle, team);

		for (Bugemon target: targets) {
			this.apply(target);
		}

		if (this.type == EffectType.SWITCH) {
			Bugemon nextBugemon = battle.getNextBugemon(team);
			battle.setActiveBugemon(nextBugemon, team);
		}

		if (this.getDuration() == EffectDuration.ROUND
				&& this.getType() == EffectType.STAT_MODIFIER) {
			Stats delta = this.buildStatsChange();
			for (Bugemon target : targets) {
				battle.getActiveEffects().add(new ActiveEffect(target, delta, 1, ""));
			}
		}
	}

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
