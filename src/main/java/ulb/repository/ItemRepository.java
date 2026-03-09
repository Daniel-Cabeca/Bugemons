package ulb.repository;

import java.lang.Iterable;
import java.util.Map;
import java.util.NoSuchElementException;

import ulb.model.item.Inventory;
import ulb.model.item.Item;

/**
 * Repository of the Items instances in the game data.
 */
public interface ItemRepository {
	/**
	 * Gets an Item instance for the corresponding id.
	 *
	 * @param id The id of the item
	 * @return The Item instance
	 * @throws NoSuchElementException If no match was found for the id
	 */
	public Item findById(String id) throws NoSuchElementException;

	/**
	 * Gives an iterable list of the items in the repository.
	 *
	 * @return An object iterable over the existing items
	 */
	public Iterable<Item> findAll();


	public Inventory getStarterInventory();
}
