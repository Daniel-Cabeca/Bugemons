package ulb.model.sample;

import ulb.model.bugemon.BugemonSpecies;
import ulb.model.type.Type;

/**
 * Hard-coded Bugemon species for testing purposes.
 */
public abstract class BugemonSpeciesSample {
	public static BugemonSpecies getA() {
		return new BugemonSpecies(
			"a",
			"A",
			Type.FLORA,
			StatsSample.getA(),
			AbilitySetSample.getABC(),
			"florachu.png",
			true
		);
	}

	public static BugemonSpecies getB() {
		return new BugemonSpecies(
			"b",
			"B",
			Type.FLORA,
			StatsSample.getA(),
			AbilitySetSample.getABC(),
			"florachu.png",
			true
		);
	}

	public static BugemonSpecies getC() {
		return new BugemonSpecies(
			"c",
			"C",
			Type.FLORA,
			StatsSample.getA(),
			AbilitySetSample.getABC(),
			"florachu.png",
			true
		);
	}

	public static BugemonSpecies getD() {
		return new BugemonSpecies(
			"d",
			"D",
			Type.FLORA,
			StatsSample.getA(),
			AbilitySetSample.getABC(),
			"florachu.png",
			true
		);
	}

	public static BugemonSpecies getE() {
		return new BugemonSpecies(
			"e",
			"E",
			Type.FLORA,
			StatsSample.getA(),
			AbilitySetSample.getABC(),
			"florachu.png",
			true
		);
	}

	public static BugemonSpecies getF() {
		return new BugemonSpecies(
			"f",
			"F",
			Type.FLORA,
			StatsSample.getA(),
			AbilitySetSample.getABC(),
			"florachu.png",
			true
		);
	}
}
