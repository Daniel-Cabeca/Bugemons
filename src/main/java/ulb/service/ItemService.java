package ulb.service;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.repository.ItemRepository;
import ulb.repository.StartingInventoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Service layer for game items.
 */
public class ItemService {
	/** Repository holding all existing item types. */
	private final ItemRepository itemRepository;
	/** Repository holding the configuration for the default starting inventory. */
	private final StartingInventoryRepository startingInventoryRepository;

	public ItemService(ItemRepository itemRepository, StartingInventoryRepository startingInventoryRepository) {
		this.itemRepository = itemRepository;
		this.startingInventoryRepository = startingInventoryRepository;
	}

	/**
	 * Creates the starter inventory for a new game, as defined in Histoire 10.
	 * Contains:
	 * - 3x Baie Revigorante
	 * - 2x Baie Tonique
	 * - 1x Gel Défensif
	 * - 1x Sérum Offensif
	 *
	 * @return The starter Inventory
	 */
	public Inventory createStarterInventory() {
		return this.startingInventoryRepository.findStartingInventory();
	}

	/**
	 * Returns a random item.
	 *
	 * @return A random item
	 */
	public Item getRandomItem() throws LoadException, EntityNotFoundException {
		List<Item> items = new ArrayList<>();
		for (Item item : this.itemRepository.findAll()) {
			items.add(item);
		}
		Random random = new Random();
		return items.get(random.nextInt(items.size()));
	}

}
