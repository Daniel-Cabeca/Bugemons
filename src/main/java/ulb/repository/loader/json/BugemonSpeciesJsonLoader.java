package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.InputStream;
import java.net.URL;
import java.io.IOException;

import ulb.repository.loader.BugemonSpeciesLoader;
import ulb.repository.loader.LoadFailureException;
import ulb.model.bugemon.BugemonSpecies;

/**
 * Loads BugemonSpecies data from a json input stream.
 */
public class BugemonSpeciesJsonLoader implements BugemonSpeciesLoader {
	private final InputStream stream;
	private final BugemonSpeciesJsonParser speciesParser;

	public BugemonSpeciesJsonLoader(InputStream stream) {
		this.stream = stream;
		this.speciesParser = new BugemonSpeciesJsonParser();
	}

	@Override
	public Iterable<BugemonSpecies> loadAll() throws LoadFailureException {
		JsonNode node = Json.getNode(this.stream);
		JsonNode bugemonsArray = node.get("bugemons");

		return this.speciesParser.parseList(bugemonsArray);
	}

	/**
	 * Gives the default input stream for loading Bugemons.
	 *
	 * @return The default input stream
	 */
	public static InputStream getDefaultStream() throws LoadFailureException {
		try {
			URL url = BugemonSpeciesJsonLoader.class.getResource("/json/bugemons.json");
			return url.openStream();
		} catch (IOException e) {
			throw new LoadFailureException(e.getMessage());
		}
	}
}
