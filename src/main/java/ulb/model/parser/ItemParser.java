package ulb.model.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ulb.model.Effect;
import ulb.model.Item;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
			String id =  itemNode.get("id").asText();
			String name =  itemNode.get("nom").asText();
			String description =  itemNode.get("description").asText();
			String category =  itemNode.get("categorie").asText();
			String sprite =  itemNode.get("sprite").asText();

			JsonNode effectNode = itemNode.get("effet");
			String type =  effectNode.get("type").asText();
			String targetType =  effectNode.get("cible").asText();

			Effect effect;
			if (Objects.equals(type, "soin")) {
				effect = new Effect(type, targetType, effectNode.get("valeur").asInt());
			} else if (Objects.equals(type, "stat_modifier")) {
				effect = new Effect(type, targetType,
						effectNode.get("stat").asText(),
						effectNode.get("modificateur").asInt(),
						effectNode.get("duree").asText());
			} else if (Objects.equals(type, "reset_malus")) {
				effect = new Effect(type, targetType);
			} else {
				throw new IllegalArgumentException("Unknown effect type: " + type);
			}

			Item item = new Item(id, name, description, category, effect, sprite);

			items.add(item);
		}

		return items;
	}
}
