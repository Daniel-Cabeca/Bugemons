package ulb.repository.json;

import java.io.InputStream;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.repository.json.parser.AbilityJsonParser;

import ulb.repository.AbilityRepository;
import ulb.model.ability.Ability;

import java.util.NoSuchElementException;

import ulb.repository.LoadException;

/**
 * An ability repository loaded from a json file.
 */
public class AbilityJsonRepository implements AbilityRepository {
	private final IdSet<Ability> abilities = new IdSet<>();

	/**
	 * Loads a repository from the default json files.
	 *
	 * @throws LoadException If loading failed
	 */
	public AbilityJsonRepository() throws LoadException {
		this(JsonResources.getStream(JsonResources.PATH_ABILITIES));
	}

	/**
	 * Loads a repository from a stream.
	 *
	 * @param stream The input stream
	 * @throws LoadException If loading failed
	 */
	public AbilityJsonRepository(InputStream stream) throws LoadException {
		JsonNode node = Json.getNode(stream);
		JsonNode abilityArray = node.get("attaques");

		AbilityJsonParser abilityParser = new AbilityJsonParser();

		for (Ability ability : abilityParser.parseList(abilityArray)) {
			this.abilities.add(ability);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Ability findById(String id) throws NoSuchElementException {
		return this.abilities.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Ability> findAll() {
		return this.abilities;
	}
}



