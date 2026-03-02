package ulb.model.sample;

import ulb.model.ability.Ability;
import ulb.model.type.Type;

//TODO effects

public abstract class AbilitySample {
	public static Ability getFouetLiane() {
		return new Ability(
			"fouet_liane",
			"Fouet-Liane",
			Type.FLORA,
			"Inflige des dégâts et réduit légèrement la défense adverse.",
			40
		);
	}

	public static Ability getPollenSournois() {
		return new Ability(
			"pollen_sournois",
			"Pollen Sournois",
			Type.FLORA,
			"Inflige des dégâts et réduit l'initiative adverse au prochain tour.",
			35
		);
	}

	public static Ability getRacinesVives() {
		return new Ability(
			"racines_vives",
			"Racines Vives",
			Type.FLORA,
			"Inflige des dégâts et augmente légèrement la défense du lanceur.",
			35
		);
	}

	public static Ability getExplosionArdente() {
		return new Ability(
			"explosion_ardente",
			"Explosion Ardente",
			Type.PYRO,
			"Inflige de très gros dégâts.",
			70
		);
	}

	public static Ability getFlammesFolles() {
		return new Ability(
			"flammes_folles",
			"Flammes Folles",
			Type.PYRO,
			"Inflige des dégâts et réduit la défense adverse.",
			45
		);
	}

	public static Ability getChocBrulant() {
		return new Ability(
			"choc_brulant",
			"Choc Brûlant",
			Type.PYRO,
			"Inflige des dégâts et augmente légèrement l'attaque du lanceur.",
			40
		);
	}
}
