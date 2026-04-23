package ulb.service;

import java.util.NoSuchElementException;

import ulb.repository.ItemRepository;
import ulb.repository.StartingInventoryRepository;
import ulb.model.item.Item;
import ulb.model.item.Inventory;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class ItemService {
	private final ItemRepository itemRepository;
	private final StartingInventoryRepository startingInventoryRepository;

	public ItemService(ItemRepository itemRepository, StartingInventoryRepository startingInventoryRepository) {
		this.itemRepository = itemRepository;
		this.startingInventoryRepository = startingInventoryRepository;
	}

	/**
	* Fetches an Item by its id.
	*
	* @param id The item's id
	* @return The Item instance
	* @throws NoSuchElementException If no item matches the id
	*/
	public Item getItem(String id) throws NoSuchElementException {
		return this.itemRepository.findById(id);
	}

	/**
	* Returns the list of all items.
	*
	* @return An iterable of all the items
	*/
	public Iterable<Item> getAllItems() {
		return this.itemRepository.findAll();
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
	public Item getRandomItem() {
		List<Item> items = new ArrayList<>();
		for (Item item : this.itemRepository.findAll()) {
			items.add(item);
		}
		Random random = new Random();
		return items.get(random.nextInt(items.size()));
	}

	/**
	 * Returns a list of all items in a given category.
	 *
	 * @param category The category to fetch items from
	 * @return The items of the given category
	 */
	public List<Item> getItemsByCategory(String category) {
		List<Item> result = new ArrayList<>();
		for (Item item : this.itemRepository.findAll()) {
			if (item.getCategory().equals(category)) {
				result.add(item);
			}
		}
		return result;
	}
}
