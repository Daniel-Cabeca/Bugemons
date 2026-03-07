package ulb.repository.loader.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ulb.model.Effect;
import ulb.model.Effect.EffectType;
import ulb.model.item.Item;

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
						effect = new Effect(Effect.EffectType.SOIN, Effect.EffectTarget.valueOf(target.toUpperCase()), 
							Map.of(Effect.StatType.PV, effectNode.get("valeur").asInt()), Effect.EffectDuration.PERMANENT);
						break;
					case "stat_modifier":
					String duration = effectNode.get("duree").asText().toUpperCase().replace("1_TOUR", "TOUR");
					effect = new Effect(EffectType.STAT_MODIFIER, Effect.EffectTarget.valueOf(target.toUpperCase()), 
						Map.of(Effect.StatType.valueOf(effectNode.get("stat").asText().toUpperCase()), 
						effectNode.get("modificateur").asInt()), 
						Effect.EffectDuration.valueOf(duration));
						break;
					case "stat_modifier_multiple":
						JsonNode modifiersNode = effectNode.get("modificateurs");
						Map<Effect.StatType, Integer> modifiers = new HashMap<>();
						modifiersNode.fields().forEachRemaining(entry ->
							modifiers.put(Effect.StatType.valueOf(entry.getKey().toUpperCase()), entry.getValue().asInt()));
					String durationMulti = effectNode.get("duree").asText().toUpperCase().replace("1_TOUR", "TOUR");
					effect = new Effect(EffectType.STAT_MODIFIER, Effect.EffectTarget.valueOf(target.toUpperCase()), 
						modifiers, Effect.EffectDuration.valueOf(durationMulti));
						break;
					case "reset_malus", "switch":
						effect = new Effect(EffectType.valueOf(type.toUpperCase()), Effect.EffectTarget.valueOf(target.toUpperCase()), 
							Map.of(), Effect.EffectDuration.TOUR);
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
