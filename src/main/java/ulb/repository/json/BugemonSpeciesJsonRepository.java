package ulb.repository.json;

import java.io.InputStream;
import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.parser.BugemonSpeciesJsonParser;

import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.BugemonSpeciesRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;

import ulb.repository.AbilityRepository;

/**
 * A Bugemon species repository loaded from a json file.
 */
public class BugemonSpeciesJsonRepository implements BugemonSpeciesRepository {
	private final IdSet<BugemonSpecies> entries = new IdSet<>();

	/**
	 * Loads a repository from the default json files.
	 *
	 * @param abilityRepository The repository to use for abilities
	 * @throws LoadException If loading failed
	 */
	public BugemonSpeciesJsonRepository(AbilityRepository abilityRepository) throws LoadException {
		this(
			JsonResources.getStream(JsonResources.PATH_BUGEMON_SPECIES),
			abilityRepository
		);
	}

	/**
	 * Loads a repository from a stream.
	 *
	 * @param stream The input stream
	 * @param abilityRepository The repository to use for abilities
	 * @throws LoadException If loading failed
	 */
	public BugemonSpeciesJsonRepository(InputStream stream, AbilityRepository abilityRepository) throws LoadException {
		JsonNode node = Json.getNode(stream);
		JsonNode bugemonArray = node.get("bugemons");

		BugemonSpeciesJsonParser bugemonParser = new BugemonSpeciesJsonParser(abilityRepository);

		for (BugemonSpecies entry: bugemonParser.parseList(bugemonArray)) {
			this.entries.add(entry);
		}
	}

	/**
	 * Loads a repository from the default json files.
	 *
	 * @throws LoadException If loading failed
	 */
	public BugemonSpeciesJsonRepository() throws LoadException {
		this(new AbilityJsonRepository());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BugemonSpecies findById(String id) throws EntityNotFoundException {
		return this.entries.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<BugemonSpecies> findAll() {
		return this.entries;
	}
}
