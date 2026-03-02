package ulb.model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.text.ParseException;
import java.net.URISyntaxException;

import ulb.model.bugemon.BugemonSpecies;
import ulb.model.bugemon.BugemonDatabase;
import ulb.model.bugemon.Stats;
import ulb.model.type.Type;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;
import ulb.model.ability.AbilityDatabase;

/**
 * Parser for Bugemon species
 */
public abstract class BugemonParser {
	/**
	 * Gives the default JSON file for loading Bugemons.
	 *
	 * @return The path to the default JSON file
	 */
	public static Path getDefaultJson() {
		try {
			URL url = BugemonParser.class.getResource("/json/bugemons.json");
			Path path = Paths.get(url.toURI());
			return path;
		} catch (URISyntaxException e) {
			// can't happen (literal is safe)
			return null;
		}
	}

	/**
	 * Loads the default JSON file into the Bugemon database.
	 */
	public static void load() throws IOException, ParseException {
		loadJson(getDefaultJson(), BugemonDatabase.getInstance());
	}

	/**
	 * Loads the default JSON file into an Bugemon database.
	 *
	 * @param database The Bugemon database
	 */
	public static void load(BugemonDatabase database) throws IOException, ParseException {
		loadJson(getDefaultJson(), database);
	}

	/**
	 * Loads a JSON file into a Bugemon database.
	 *
	 * @param filename The JSON file's path
	 * @param database The Bugemon database
	 */
	public static void loadJson(Path filename, BugemonDatabase database) throws IOException, ParseException {
		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(filename.toFile());
		JsonNode bugemonsArray = root.get("bugemons");

		for (JsonNode node: bugemonsArray) {
			BugemonSpecies species = fromJson(node);
			database.add(species);
		}
	}

	/**
	 * Reads a Bugemon from a JSON node.
	 *
	 * @param node The JSON node
	 * @return The parsed Bugemon
	 */
	public static BugemonSpecies fromJson(JsonNode node) throws ParseException {
		String id = node.get("id").asText();
		String name = node.get("nom").asText();
		Type type = readJsonType(node);
		Stats baseStats = readJsonStats(node);
		AbilitySet abilities = readJsonAbilities(node);
		String sprite = node.get("sprite").asText();
		boolean starter = node.get("starter").asBoolean();

		return new BugemonSpecies(id, name, type, baseStats, abilities, sprite, starter);
	}

	private static Type readJsonType(JsonNode node) throws ParseException {
		try {
			String type_str = node.get("type").asText();
			return TypeParser.fromString(type_str);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Failed to parse type.", 0);
		}
	}

	private static Stats readJsonStats(JsonNode node) {
		node = node.get("stats");

		int hp = node.get("pv").asInt();
		int attack = node.get("attaque").asInt();
		int defense = node.get("defense").asInt();
		int initiative = node.get("initiative").asInt();

		return new Stats(hp, attack, defense, initiative);
	}

	private static AbilitySet readJsonAbilities(JsonNode node) {
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
