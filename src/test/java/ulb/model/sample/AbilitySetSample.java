package ulb.model.sample;

import ulb.model.ability.AbilitySet;

/**
 * Hard-coded ability sets for testing purposes.
 */
public abstract class AbilitySetSample {
	public static AbilitySet getABC() {
		return new AbilitySet(AbilitySample.getA(), AbilitySample.getB(), AbilitySample.getC());
	}
}
