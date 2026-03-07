package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.InputStream;
import java.net.URL;
import java.io.IOException;

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

	/**
	 * Gives the default input stream for loading Bugemons.
	 *
	 * @return The default input stream
	 */
	public static InputStream getDefaultStream() throws LoadFailureException {
		try {
			URL url = AbilityJsonLoader.class.getResource("/json/attaques.json");
			return url.openStream();
		} catch (IOException e) {
			throw new LoadFailureException(e.getMessage());
		}
	}
}
