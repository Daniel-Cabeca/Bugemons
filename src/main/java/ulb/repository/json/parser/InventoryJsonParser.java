package ulb.repository.json.parser;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Map;
import java.util.Iterator;

import ulb.model.item.Item;
import ulb.model.item.Inventory;

import ulb.repository.ItemRepository;

import ulb.exceptions.LoadException;
import java.util.NoSuchElementException;

/**
 * Json parser for item inventories.
 */
public class InventoryJsonParser {
	private final ItemRepository itemRepository;

	public InventoryJsonParser(ItemRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	/**
	 * Parse an inventory from a json node.
	 *
	 * @param node The json node
	 * @return The parsed inventory
	 * @throws LoadException If parsing fails or contains an unknown item
	 */
	public Inventory parseOne(JsonNode node) throws LoadException {
		Inventory inventory = new Inventory();
		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> field = fields.next();

			String id = field.getKey();
			int quantity = field.getValue().asInt();

			try {
				Item item = this.itemRepository.findById(id);
				inventory.addItem(item, quantity);
			} catch (NoSuchElementException e) {
				throw new LoadException("Item "+ id +" does not exist");
			}
		}

		return inventory;
	}
}
