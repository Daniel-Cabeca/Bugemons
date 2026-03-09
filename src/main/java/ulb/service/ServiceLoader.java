package ulb.service;

import ulb.repository.loader.LoadException;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.json.BugemonSpeciesJsonRepository;

/**
 * Gives access to services.
 * Should eventually be deprecated ; services should be instanciated outside and injected into objects' constructors.
 */
public abstract class ServiceLoader {
	private static BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesJsonRepository();

	private static BugemonService bugemonService = new BugemonService(bugemonRepository);

	public static BugemonService getBugemonService() {
		return bugemonService;
	}
}
