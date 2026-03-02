package ulb.model.sample;

import ulb.model.bugemon.Stats;

public abstract class StatsSample {
	public static Stats getFlorachu() {
		return new Stats(90, 55, 40, 50);
	}

	public static Stats getExceflam() {
		return new Stats(85, 65, 40, 60);
	}
}
