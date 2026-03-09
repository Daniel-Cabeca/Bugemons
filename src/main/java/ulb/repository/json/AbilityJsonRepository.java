package ulb.repository.json;

import java.util.NoSuchElementException;

import ulb.repository.AbilityRepository;
import ulb.repository.inmemory.AbilityInMemoryRepository;
import ulb.model.ability.Ability;

import ulb.repository.loader.LoadException;
import ulb.repository.loader.json.JsonResources;
import java.io.InputStream;
import ulb.repository.loader.AbilityLoader;
import ulb.repository.loader.json.AbilityJsonLoader;

/**
 * An ability repository loaded from a json file.
 */
public class AbilityJsonRepository implements AbilityRepository {
	private final AbilityRepository loadedAbilityRepository;

	/**
	 * Loads a repository from the default json files.
	 *
	 * @throws LoadException If loading failed
	 */
	public AbilityJsonRepository() throws LoadException {
		String path = JsonResources.PATH_ABILITIES;
		InputStream stream = JsonResources.getStream(path);
		AbilityLoader loader = new AbilityJsonLoader(stream);

		this.loadedAbilityRepository = new AbilityInMemoryRepository(loader.loadAll());
	}

	@Override
	public Ability findById(String id) throws NoSuchElementException {
		return this.loadedAbilityRepository.findById(id);
	}
}
