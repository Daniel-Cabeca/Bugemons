package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.ArrayList;

import ulb.model.item.Item;
import ulb.model.effect.Effect;

import ulb.repository.LoadException;

public class ItemJsonParser {
	private final EffectJsonParser effectParser = new EffectJsonParser();

	/**
	 * Parses an item from a json node.
	 *
	 * @param node The json node
	 * @return The parsed item
	 * @throws LoadException If parsing failed
	 */
	public Item parseOne(JsonNode node) throws LoadException {
		String id = node.get("id").asText();
		String name = node.get("nom").asText();
		String description = node.get("description").asText();
		String category = node.get("categorie").asText();
		Effect effect = this.effectParser.parseOne(node.get("effet"));
		String sprite = node.get("sprite").asText();

		return new Item(id, name, description, category, effect, sprite);
	}

	/**
	 * Parses a list of items from a json node.
	 *
	 * @param node The json node
	 * @return The parsed items
	 * @throws LoadException If parsing failed
	 */
	public Iterable<Item> parseList(JsonNode node) throws LoadException {
		List<Item> items = new ArrayList<>();

		for (JsonNode itemNode: node) {
			Item item = this.parseOne(itemNode);
			items.add(item);
		}

		return items;
	}
}
