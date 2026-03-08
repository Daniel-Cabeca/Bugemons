package ulb.repository.loader.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.ArrayList;

import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.BugemonDatabase;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.ability.AbilityDatabase;

import ulb.repository.loader.LoadException;

/**
 * Parser for Bugemon species
 */
public class BugemonSpeciesJsonParser {
	private final TypeJsonParser typeParser;

	public BugemonSpeciesJsonParser() {
		this.typeParser = new TypeJsonParser();
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
			BugemonSpecies species = parseOne(node);

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
		Stats baseStats = readJsonStats(node);
		AbilitySet abilities = readJsonAbilities(node);
		String sprite = node.get("sprite").asText();
		boolean starter = node.get("starter").asBoolean();

		return new BugemonSpecies(id, name, type, baseStats, abilities, sprite, starter);
	}

	private Stats readJsonStats(JsonNode node) {
		node = node.get("stats");

		int hp = node.get("pv").asInt();
		int attack = node.get("attaque").asInt();
		int defense = node.get("defense").asInt();
		int initiative = node.get("initiative").asInt();

		return new Stats(hp, attack, defense, initiative);
	}

	private AbilitySet readJsonAbilities(JsonNode node) {
		AbilitySet abilities = new AbilitySet();
		JsonNode abilitiesArray = node.get("attaques");
		int i = 0;

		for (JsonNode abilityNode: abilitiesArray) {
			String id = abilityNode.asText();
			Ability ability = AbilityDatabase.getInstance().get(id);
			abilities.setAbility(i++, ability);
		}

		return abilities;
	}
}
