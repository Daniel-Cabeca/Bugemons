package ulb.repository;

import ulb.model.item.Inventory;

public interface StartingInventoryRepository {

	/**
	 * Gets the starting inventory.
	 * Contains:
	 * - 3x Baie Revigorante
	 * - 2x Baie Tonique
	 * - 1x Gel Défensif
	 * - 1x Sérum Offensif
	 *
	 * @return Inventory containing the starting items
	 */
	public Inventory findStartingInventory();
}
