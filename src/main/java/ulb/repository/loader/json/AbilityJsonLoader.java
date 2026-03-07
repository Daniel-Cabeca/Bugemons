package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;

import ulb.repository.loader.AbilityLoader;
import ulb.repository.loader.LoadFailureException;
import ulb.model.ability.Ability;

/**
 * Loads Ability data from a json input stream.
 */
public class AbilityJsonLoader implements AbilityLoader {
	private final InputStream stream;
	private final AbilityJsonParser abilityParser;

	public AbilityJsonLoader(InputStream stream) {
		this.stream = stream;
		this.abilityParser = new AbilityJsonParser();
	}

	@Override
	public Iterable<Ability> loadAll() throws LoadFailureException {
		JsonNode node = Json.getNode(this.stream);
		JsonNode abilityArray = node.get("attaques");

		return this.abilityParser.parseList(abilityArray);
	}
}
