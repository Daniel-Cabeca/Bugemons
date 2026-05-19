package ulb.model.effect;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;

import java.util.ArrayList;
import java.util.List;

public class EffectList {
	private final List<Effect> effects = new ArrayList<>();

	public EffectList() {
	}

	public int getSize() { return this.effects.size(); }

	/**
	 * Adds an effect to the effect list.
	 *
	 * @param effect The effect to add
	 */
	public void add(Effect effect) {
		this.effects.add(effect);
	}

	/**
	 * Applies effects to the chosen team
	 *
	 * @param battle The current battle
	 * @param team The chosen team
	 */
	public void apply(Battle battle, ParticipantLabel team) {
		for (Effect effect : this.effects) {
			effect.apply(battle, team);
		}
	}

	public List<Effect> getEffects() { return this.effects; }
}
