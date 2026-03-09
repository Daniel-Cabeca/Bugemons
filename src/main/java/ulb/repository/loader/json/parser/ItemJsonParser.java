package ulb.repository.loader.json.parser;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.ArrayList;

import ulb.model.item.Item;
import ulb.model.Effect;

import ulb.repository.loader.LoadException;




//TODO remove imports below

import com.fasterxml.jackson.databind.ObjectMapper;
import ulb.model.Effect;
import ulb.model.Effect.EffectType;
import ulb.model.item.Item;

import java.io.File;
import java.io.IOException;
import java.util.*;

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

	/**
	 * Loads all items from a JSON file
	 * @param path the path to the JSON file containing the items
	 * @return a list of loaded Item objects
	 * @throws IOException if the file cannot be read or the JSON is malformed
	 * @throws IllegalArgumentException if an item contains an unknown effect type
	 */
	public static List<Item> loadItems(String path) throws IOException, IllegalArgumentException {
		//TODO move to ItemJsonLoader class

		ItemJsonParser itemParser = new ItemJsonParser();



		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(new File(path));

        JsonNode itemsNode = root.get("objets");
		List<Item> items = (List<Item>) itemParser.parseList(itemsNode);

		return items;
	}

	/**
	 * Loads the starting inventory from a JSON file
	 *
	 * @param path the path to the JSON file containing the inventory
	 * @return a map of item id to quantity
	 * @throws IOException if the file cannot be read or JSON is malformed
	 */
	public static Map<String, Integer> loadInventory(String path) throws IOException {
		//TODO move to ItemJsonLoader class or InventoryLoader class

		Map<String, Integer> inventory = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(new File(path));
		JsonNode inventoryNode = root.get("inventaire_depart");

		inventoryNode.fields().forEachRemaining(entry ->
				inventory.put(entry.getKey(), entry.getValue().asInt()));

		return inventory;
	}
}
