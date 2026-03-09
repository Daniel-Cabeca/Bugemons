package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.io.InputStream;

import ulb.repository.loader.BugemonSpeciesLoader;
import ulb.repository.loader.json.parser.BugemonSpeciesJsonParser;
import ulb.repository.loader.LoadException;
import ulb.repository.AbilityRepository;
import ulb.model.bugemon.BugemonSpecies;

/**
 * Loads BugemonSpecies data from a json input stream.
 */
public class BugemonSpeciesJsonLoader implements BugemonSpeciesLoader {
	private final InputStream stream;
	private final BugemonSpeciesJsonParser speciesParser;

	public BugemonSpeciesJsonLoader(InputStream stream, AbilityRepository abilityRepository) {
		this.stream = stream;
		this.speciesParser = new BugemonSpeciesJsonParser(abilityRepository);
	}

	@Override
	public Iterable<BugemonSpecies> loadAll() throws LoadException {
		JsonNode node = Json.getNode(this.stream);
		JsonNode bugemonsArray = node.get("bugemons");

		return this.speciesParser.parseList(bugemonsArray);
	}
}
