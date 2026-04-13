package ulb.model.battle;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

/**
 * Active temporary effect applied to a bugemon in battle.
 */
public class ActiveEffect {
	final Bugemon target;
	final Stats delta;
	final String itemName;
	int ttl;

	/**
	 * Creates an active effect instance.
	 *
	 * @param target Target bugemon
	 * @param delta Stats delta applied
	 * @param ttl Remaining turns
	 * @param itemName Source item name
	 */
	public ActiveEffect(Bugemon target, Stats delta, int ttl, String itemName) {
		this.target = target;
		this.delta = new Stats(delta);
		this.ttl = ttl;
		this.itemName = itemName;
	}
}
