package ulb.repository.json;

import com.fasterxml.jackson.databind.JsonNode;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.repository.ItemRepository;
import ulb.repository.json.parser.InventoryJsonParser;
import ulb.repository.json.parser.ItemJsonParser;

import java.io.InputStream;

/**
 * An item repository loaded from a json file.
 */
public class ItemJsonRepository implements ItemRepository {
	/** Container for all loaded items. */
	private final IdSet<Item> items = new IdSet<>();

	/** The default starting inventory. */
	private final Inventory startingInventory;

	/**
	 * Loads a repository from the default json files.
	 *
	 * @throws LoadException If loading failed
	 */
	public ItemJsonRepository() throws LoadException {
		this(JsonResources.getStream(JsonResources.PATH_ITEMS));
	}

	/**
	 * Loads a repository from a stream.
	 *
	 * @param stream The input stream
	 * @throws LoadException If loading failed
	 */
	public ItemJsonRepository(InputStream stream) throws LoadException {
		JsonNode node = Json.getNode(stream);
		JsonNode itemArray = node.get("objets");
		JsonNode startingInventoryNode = node.get("inventaire_depart");

		ItemJsonParser itemParser = new ItemJsonParser();
		for (Item item : itemParser.parseList(itemArray)) {
			this.items.add(item);
		}

		InventoryJsonParser inventoryParser = new InventoryJsonParser(this);
		this.startingInventory = inventoryParser.parseOne(startingInventoryNode);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item findById(String id) throws EntityNotFoundException {
		return this.items.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Item> findAll() {
		return this.items;
	}


	public Inventory getStartingInventory() { return this.startingInventory; }
}
