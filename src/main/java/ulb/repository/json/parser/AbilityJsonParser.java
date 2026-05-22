package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.exceptions.LoadException;
import ulb.model.ability.Ability;
import ulb.model.effect.EffectList;
import ulb.model.type.Type;

import java.util.ArrayList;
import java.util.List;

/**
 * Json parser for abilities.
 */
public class AbilityJsonParser {
	/** The parser for ability types. */
	private final TypeJsonParser typeParser;
	/** The parser for ability effects. */
	private final EffectJsonParser effectParser;

	public AbilityJsonParser() {
		this.typeParser = new TypeJsonParser();
		this.effectParser = new EffectJsonParser();
	}

	/**
	 * Parse abilities from a json node.
	 *
	 * @param node The JSON node to parser
	 */
	public Iterable<Ability> parseList(JsonNode node) throws LoadException {
		List<Ability> res = new ArrayList<>();

		for (JsonNode abilityNode : node) {
			Ability ability = parseOne(abilityNode);
			res.add(ability);
		}

		return res;
	}

	/**
	 * Reads an ability from a JSON node.
	 *
	 * @param node The JSON node to parse
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
