package ulb.model.sample;

import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

public abstract class BugemonSpeciesSample {
	public static BugemonSpecies getFlorachu() {
		return new BugemonSpecies(
			"florachu",
			"Florachu",
			Type.FLORA,
			StatsSample.getFlorachu(),
			AbilitySetSample.getFlorachu(),
			"florachu.png",
			true
		);
	}

	public static BugemonSpecies getExceflam() {
		return new BugemonSpecies(
			"exceflam",
			"Exceflam",
			Type.PYRO,
			StatsSample.getExceflam(),
			AbilitySetSample.getExceflam(),
			"exceflam.png",
			true
		);
	}
}
