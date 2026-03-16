package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.ArrayList;

import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;
import ulb.model.ability.AbilitySet;

import ulb.repository.AbilityRepository;
import ulb.repository.LoadException;

/**
 * Parser for Bugemon species
 */
public class BugemonSpeciesJsonParser {
	private final TypeJsonParser typeParser;
	private final AbilitySetJsonParser abilitySetParser;

	public BugemonSpeciesJsonParser(AbilityRepository abilityRepository) {
		this.typeParser = new TypeJsonParser();
		this.abilitySetParser = new AbilitySetJsonParser(abilityRepository);
	}

	/**
	 * Parse species from a json node.
	 *
	 * @param filename The json node
	 * @throws LoadException If the parsing failed
	 */
	public Iterable<BugemonSpecies> parseList(JsonNode node) throws LoadException {
		List<BugemonSpecies> res = new ArrayList<>();

		for (JsonNode speciesNode: node) {
			BugemonSpecies species = parseOne(speciesNode);
			res.add(species);
		}

		return res;
	}

	/**
	 * Parse one species from a json node.
	 *
	 * @param node The json node
	 * @return The parsed species
	 */
	public BugemonSpecies parseOne(JsonNode node) throws LoadException {
		String id = node.get("id").asText();
		String name = node.get("nom").asText();
		Type type = this.typeParser.parseOne(node.get("type"));
		Stats baseStats = this.parseStats(node);
		AbilitySet abilities = this.abilitySetParser.parseOne(node.get("attaques"));
		String sprite = node.get("sprite").asText();
		boolean starter = node.get("starter").asBoolean();

		return new BugemonSpecies(id, name, type, baseStats, abilities, sprite, starter);
	}

	/**
	 * Parse stats from a jspon node.
	 *
	 * @param node The json node
	 * @return The parsed stats
	 */
	public Stats parseStats(JsonNode node) {
		node = node.get("stats");

		int hp = node.get("pv").asInt();
		int attack = node.get("attaque").asInt();
		int defense = node.get("defense").asInt();
		int initiative = node.get("initiative").asInt();

		return new Stats(hp, attack, defense, initiative);
	}
}
