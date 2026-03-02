package ulb.model.sample;

import ulb.model.ability.AbilityDatabase;
import ulb.model.bugemon.BugemonDatabase;

/**
 * Uses samples to populate the database.
 */
public abstract class SamplesLoader {
	private static boolean isLoaded = false;

	public static void load() {
		if (!isLoaded) {
			loadAbilities();
			loadBugemons();

			isLoaded = true;
		}
	}

	private static void loadAbilities() {
		AbilityDatabase.getInstance().add(AbilitySample.getA());
		AbilityDatabase.getInstance().add(AbilitySample.getB());
		AbilityDatabase.getInstance().add(AbilitySample.getC());
		AbilityDatabase.getInstance().add(AbilitySample.getD());
		AbilityDatabase.getInstance().add(AbilitySample.getE());
	}

	private static void loadBugemons() {
		BugemonDatabase.getInstance().add(BugemonSpeciesSample.getA());
		BugemonDatabase.getInstance().add(BugemonSpeciesSample.getB());
		BugemonDatabase.getInstance().add(BugemonSpeciesSample.getC());
		BugemonDatabase.getInstance().add(BugemonSpeciesSample.getD());
		BugemonDatabase.getInstance().add(BugemonSpeciesSample.getE());
		BugemonDatabase.getInstance().add(BugemonSpeciesSample.getF());
	}
}
