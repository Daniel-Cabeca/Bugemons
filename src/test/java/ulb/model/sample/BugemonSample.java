package ulb.model.sample;

import ulb.model.bugemon.Bugemon;
import ulb.model.bugemon.Stats;

/**
 * Hard-coded Bugemons for testing purposes.
 */
public abstract class BugemonSample {
	public static Bugemon getA() {
		return new Bugemon(BugemonSpeciesSample.getA());
	}

	public static Bugemon getB() {
		return new Bugemon(BugemonSpeciesSample.getB());
	}

	public static Bugemon getC() {
		return new Bugemon(BugemonSpeciesSample.getC());
	}

	public static Bugemon getD() {
		return new Bugemon(BugemonSpeciesSample.getD());
	}

	public static Bugemon getE() {
		return new Bugemon(BugemonSpeciesSample.getE());
	}

	public static Bugemon getF() {
		return new Bugemon(BugemonSpeciesSample.getF());
	}

	public static Bugemon getG() {

		Bugemon a = new Bugemon(BugemonSpeciesSample.getF());
		a.changeFightStats(new Stats(-10, 0, 0, 0));
		return a;
	}
}
