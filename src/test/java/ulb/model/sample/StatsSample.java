package ulb.model.sample;

import ulb.model.bugemon.Stats;

/**
 * Hard-coded stats for testing purposes.
 */
public abstract class StatsSample {
	public static Stats getA() {
		return new Stats(100, 10, 10, 10);
	}
}
