package ulb.model.effect;

import java.util.List;
import java.util.ArrayList;

import ulb.model.battle.Battle;
import ulb.model.battle.Battle.TeamLabel;

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

	public void apply(Battle battle, TeamLabel team) {
		for (Effect effect: this.effects) {
			effect.apply(battle, team);
		}
	}
}
