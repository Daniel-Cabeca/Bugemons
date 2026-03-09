package ulb.repository.json;

import java.util.NoSuchElementException;

import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.inmemory.BugemonSpeciesInMemoryRepository;
import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.AbilityRepository;

import ulb.repository.loader.LoadException;
import ulb.repository.loader.json.JsonResources;
import java.io.InputStream;
import ulb.repository.loader.BugemonSpeciesLoader;
import ulb.repository.loader.json.BugemonSpeciesJsonLoader;

/**
 * A Bugemon species repository loaded from a json file.
 */
public class BugemonSpeciesJsonRepository implements BugemonSpeciesRepository {
	private final BugemonSpeciesRepository loadedSpeciesRepository;

	/**
	 * Loads a repository from the default json files.
	 *
	 * @throws LoadException If loading failed
	 */
	public BugemonSpeciesJsonRepository(AbilityRepository abilityRepository) throws LoadException {
		String path = JsonResources.PATH_BUGEMON_SPECIES;
		InputStream stream = JsonResources.getStream(path);
		BugemonSpeciesLoader loader = new BugemonSpeciesJsonLoader(stream, abilityRepository);

		this.loadedSpeciesRepository = new BugemonSpeciesInMemoryRepository(loader.loadAll());
	}

	@Override
	public BugemonSpecies findById(String id) throws NoSuchElementException {
		return this.loadedSpeciesRepository.findById(id);
	}

	@Override
	public Iterable<BugemonSpecies> findAll() {
		return this.loadedSpeciesRepository.findAll();
	}
}
