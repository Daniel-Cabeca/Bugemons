package ulb.model.sample;

import ulb.model.ability.Ability;
import ulb.model.type.Type;

/**
 * Hard-coded abilities for testing purposes.
 */
public abstract class AbilitySample {
	public static Ability getA() {
		return new Ability(
			"a",
			"A",
			Type.FLORA,
			"Inflige des dégâts.",
			40
		);
	}

	public static Ability getB() {
		return new Ability(
			"b",
			"B",
			Type.FLORA,
			"Inflige des dégâts.",
			40
		);
	}

	public static Ability getC() {
		return new Ability(
			"c",
			"C",
			Type.FLORA,
			"Inflige des dégâts.",
			40
		);
	}

	public static Ability getD() {
		return new Ability(
			"d",
			"D",
			Type.FLORA,
			"Inflige des dégâts.",
			40
		);
	}

	public static Ability getE() {
		return new Ability(
			"e",
			"E",
			Type.FLORA,
			"Inflige des dégâts.",
			40
		);
	}

	public static Ability getF(){
		return new Ability(
				"e",
				"E",
				Type.FLORA,
				"Inflige des dégâts et soigne.",
				40,
				EffectSample.getHeal()
		);
	}

	public static Ability getG(){
		return new Ability(
				"g",
				"G",
				Type.FLORA,
				"Inflige des dégâts et diminue la défense adverse.",
				40,
				EffectSample.getDefenseDecreaseOther()
		);
	}

	public static Ability getH(){
		return new Ability(
				"h",
				"H",
				Type.FLORA,
				"Inflige aucun dégat",
				0
		);
	}

	public static Ability getI(){
		return new Ability(
				"i",
				"I",
				Type.FLORA,
				"Inflige des dégats gigantesques",
				1000
		);
	}
}
