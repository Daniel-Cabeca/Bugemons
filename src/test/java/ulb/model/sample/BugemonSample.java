package ulb.model.sample;

import ulb.model.bugemon.Bugemon;

public abstract class BugemonSample {
	public static Bugemon getFlorachu() {
		return new Bugemon(BugemonSpeciesSample.getFlorachu());
	}

	public static Bugemon getExceflam() {
		return new Bugemon(BugemonSpeciesSample.getExceflam());
	}
}
