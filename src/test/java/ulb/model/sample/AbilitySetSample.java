package ulb.model.sample;

import ulb.model.ability.AbilitySet;

public abstract class AbilitySetSample {
	public static AbilitySet getFlorachu() {
		return new AbilitySet(AbilitySample.getFouetLiane(), AbilitySample.getPollenSournois(), AbilitySample.getRacinesVives());
	}

	public static AbilitySet getExceflam() {
		return new AbilitySet(AbilitySample.getExplosionArdente(), AbilitySample.getFlammesFolles(), AbilitySample.getChocBrulant());
	}
}
