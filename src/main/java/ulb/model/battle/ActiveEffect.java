package ulb.model.battle;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

class ActiveEffect {
	final Bugemon target;
	final Stats delta;
	int ttl;

	ActiveEffect(Bugemon target, Stats delta, int ttl) {
		this.target = target;
		this.delta = new Stats(delta);
		this.ttl = ttl;
	}
}
