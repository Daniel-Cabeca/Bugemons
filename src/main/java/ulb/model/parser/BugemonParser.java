package ulb.model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.Vector;

import java.text.ParseException;

import ulb.model.Bugemon;
import ulb.model.type.Type;
import ulb.model.ability.Ability;
import ulb.model.ability.AbilitySet;

public abstract class BugemonParser {

	public static Vector<Bugemon> loadBugemons(String path) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(new File(path));
		JsonNode bugemonArray = root.get("bugemons");
		Vector<Bugemon> bugemons = new Vector<>();

		for (JsonNode node : bugemonArray) {
			Bugemon bugemon = fromJson(node);
			bugemons.add(bugemon);
		}

		return bugemons;
	}

	/**
	 * Reads a Bugemon from a JSON node.
	 *
	 * @param node The JSON node
	 * @return The parsed Bugemon
	 */
	public static Bugemon fromJson(JsonNode node) throws ParseException {
		// String id = node.get("id").asText(); //TODO add id to Bugemon class
		String name = node.get("nom").asText();
		String sprite = node.get("sprite").asText();
		Type type = readJsonType(node);

		JsonNode statsNode = node.get("stats");
		int pv = statsNode.get("pv").asInt();
		int attack = statsNode.get("attaque").asInt();
		int defense = statsNode.get("defense").asInt();
		int initiative = statsNode.get("initiative").asInt();

		int level = 1;

		//TODO abilities

		return new Bugemon(name,sprite, type, pv, attack, defense, initiative, level);
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
