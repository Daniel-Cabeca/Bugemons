package ulb.model.sample;

import ulb.model.bugemon.Bugemon;

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
}
