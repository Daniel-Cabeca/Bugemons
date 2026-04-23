package ulb.repository;

import java.util.NoSuchElementException;

import ulb.model.item.Inventory;
import ulb.model.item.Item;

/**
 * Repository of the Items instances in the game data.
 */
public interface InventoryRepository {
	/**
	 * Gives a player starting inventory.
	 *
	 * @return A starting inventory
	 */
	// public Inventory findStartingInventory();

	public void insertItem(Item item, int quantity, String username) throws LoadException;

	public void insertInventory(Inventory inventory, String username) throws LoadException;

	public Inventory getInventory(String username) throws NoSuchElementException;
}
