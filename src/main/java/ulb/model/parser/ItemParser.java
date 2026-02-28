package ulb.model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ulb.model.Effect;
import ulb.model.Item;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ItemParser {

	/**
	 * Loads all items from a JSON file
	 * @param path the path to the JSON file containing the items
	 * @return a list of loaded Item objects
	 * @throws IOException if the file cannot be read or the JSON is malformed
	 * @throws IllegalArgumentException if an item contains an unknown effect type
	 */
	public static List<Item> loadItems(String path) throws IOException, IllegalArgumentException {

		List<Item> items = new ArrayList<>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(new File(path));
        JsonNode itemsNode = root.get("objets");

		for (JsonNode itemNode : itemsNode) {
			try {
				String id = itemNode.get("id").asText();
				String name = itemNode.get("nom").asText();
				String description = itemNode.get("description").asText();
				String category = itemNode.get("categorie").asText();
				String sprite = itemNode.get("sprite").asText();

				JsonNode effectNode = itemNode.get("effet");
				String type = effectNode.get("type").asText();
				String target = effectNode.get("cible").asText();

				Effect effect;

				switch (type) {
					case "soin":
						effect = new Effect(type, target, effectNode.get("valeur").asInt());
						break;
					case "stat_modifier":
						effect = new Effect(type, target,
								effectNode.get("stat").asText(),
								effectNode.get("modificateur").asInt(),
								effectNode.get("duree").asText());
						break;
					case "stat_modifier_multiple":
						JsonNode modifiersNode = effectNode.get("modificateurs");
						Map<String, Integer> modifiers = new HashMap<>();
						modifiersNode.fields().forEachRemaining(entry ->
								modifiers.put(entry.getKey(), entry.getValue().asInt()));
						effect = new Effect(type, target, modifiers, effectNode.get("duree").asText());
						break;
					case "reset_malus", "switch":
						effect = new Effect(type, target);
						break;
					default:
						throw new IllegalArgumentException("Unknown effect type: " + type);
				}

				Item item = new Item(id, name, description, category, effect, sprite);

				items.add(item);
			}
			catch (Exception e) {
				System.err.println("Skipping invalid item: " + e.getMessage());
			}
		}

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
		Map<String, Integer> inventory = new HashMap<>();

		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(new File(path));
		JsonNode inventoryNode = root.get("inventaire_depart");

		inventoryNode.fields().forEachRemaining(entry ->
				inventory.put(entry.getKey(), entry.getValue().asInt()));

		return inventory;
	}
}
