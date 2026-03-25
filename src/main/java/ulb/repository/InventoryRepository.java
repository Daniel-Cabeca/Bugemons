package ulb.repository;

import ulb.model.item.Inventory;

/**
 * Repository of the Items instances in the game data.
 */
public interface InventoryRepository {
	/**
	 * Gives a player starting inventory.
	 *
	 * @return A starting inventory
	 */
	public Inventory findStartingInventory();
}
