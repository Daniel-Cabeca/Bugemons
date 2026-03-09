package ulb.service;

import ulb.repository.loader.LoadException;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.json.BugemonSpeciesJsonRepository;

/**
 * TODO temporary class
 * services need to be injected into objects that need them
 * this class is a step before that refactoring
 */
public abstract class ServiceLoader {
	private static BugemonService bugemonService;

	public static void load() throws LoadException {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesJsonRepository();
		bugemonService = new BugemonService(bugemonRepository);
	}

	public static BugemonService getBugemonService() {
		return bugemonService;
	}
}
