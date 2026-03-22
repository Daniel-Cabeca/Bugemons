package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.ArrayList;

import ulb.model.ability.Ability;
import ulb.model.type.Type;
import ulb.model.effect.EffectList;

import ulb.repository.LoadException;

/**
 * Json parser for abilities.
 */
public class AbilityJsonParser {
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
	public Iterable<Ability> parseList(JsonNode node) throws LoadException {
		List<Ability> res = new ArrayList<>();

		for (JsonNode abilityNode: node) {
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
	public Ability parseOne(JsonNode node) throws LoadException {
		String id = node.get("id").asText();
		String name = node.get("nom").asText();
		Type type = this.typeParser.parseOne(node.get("type"));
		String description = node.get("description").asText();
		int power = node.get("puissance").asInt();
		EffectList effects = this.effectParser.parseList(node.get("effets"));

		return new Ability(id, name, type, description, power, effects);
	}
}
