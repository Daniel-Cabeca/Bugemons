package ulb.model.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.text.ParseException;
import java.net.URISyntaxException;

import ulb.model.ability.Ability;
import ulb.model.ability.AbilityDatabase;
import ulb.model.type.Type;

/**
 * Parser for abilities.
 */
public abstract class AbilityParser {
	/**
	 * Gives the default JSON file for loading abilities.
	 *
	 * @return The path to the default JSON file
	 */
	public static Path getDefaultJson() {
		try {
			URL url = BugemonParser.class.getResource("/json/attaques.json");
			Path path = Paths.get(url.toURI());
			return path;
		} catch (URISyntaxException e) {
			// can't happen (literal is safe)
			return null;
		}
	}

	/**
	 * Loads the default JSON file into the ability database.
	 */
	public static void load() throws IOException, ParseException {
		loadJson(getDefaultJson(), AbilityDatabase.getInstance());
	}

	/**
	 * Loads the default JSON file into an ability database.
	 *
	 * @param database The ability database
	 */
	public static void load(AbilityDatabase database) throws IOException, ParseException {
		loadJson(getDefaultJson(), database);
	}

	/**
	 * Loads a JSON file into an ability database.
	 *
	 * @param filename The JSON file's path
	 * @param database The ability database
	 */
	public static void loadJson(Path filename, AbilityDatabase database) throws IOException, ParseException {
		ObjectMapper mapper = new ObjectMapper();

		JsonNode root = mapper.readTree(filename.toFile());
		JsonNode abilitiesArray = root.get("attaques");

		for (JsonNode node: abilitiesArray) {
			Ability ability = fromJson(node);
			database.add(ability);
		}
	}

	/**
	 * Reads an ability from a JSON node.
	 *
	 * @param node The JSON node
	 * @return The parsed ability
	 * @throws ParseException If parsing fails
	 */
	public static Ability fromJson(JsonNode node) throws ParseException {
		String id = node.get("id").asText();
		String name = node.get("nom").asText();
		Type type = readJsonType(node);
		String description = node.get("description").asText();
		int power = node.get("puissance").asInt();

		//TODO effects

		return new Ability(id, name, type, description, power);
	}

	private static Type readJsonType(JsonNode node) throws ParseException {
		try {
			String type_str = node.get("type").asText();
			return TypeParser.fromString(type_str);
		} catch (IllegalArgumentException e) {
			throw new ParseException("Failed to parse type.", 0);
		}
	}
}
