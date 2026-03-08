package ulb.repository.loader.json;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.ArrayList;

import java.io.IOException;
import java.text.ParseException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import ulb.model.ability.Ability;
import ulb.model.type.Type;
import ulb.model.Effect;

import ulb.repository.loader.LoadFailureException;

/**
 * Json parser for abilities.
 */
class AbilityJsonParser {
	private final TypeJsonParser typeParser;
	private final EffectJsonParser effectParser;

	public AbilityJsonParser() {
		this.typeParser = new TypeJsonParser();
		this.effectParser = new EffectJsonParser();
	}

	/**
	 * Parse abilities from a json node.
	 *
	 * @param filename The JSON file's path
	 * @param database The ability database
	 */
	public Iterable<Ability> parseList(JsonNode node) throws LoadFailureException {
		List<Ability> res = new ArrayList<>();
		JsonNode abilitiesArray = node.get("attaques");

		for (JsonNode abilityNode: abilitiesArray) {
			Ability ability = parseOne(abilityNode);
			res.add(ability);
		}

		return res;
	}

	/**
	 * Reads an ability from a JSON node.
	 *
	 * @param node The JSON node
	 * @return The parsed ability
	 */
	public Ability parseOne(JsonNode node) throws LoadFailureException {
		String id = node.get("id").asText();
		String name = node.get("nom").asText();
		Type type = this.typeParser.parseOne(node.get("type"));
		String description = node.get("description").asText();
		int power = node.get("puissance").asInt();
		Effect effect = this.effectParser.parseList(node.get("effets"));

		return new Ability(id, name, type, description, power);
	}
}
