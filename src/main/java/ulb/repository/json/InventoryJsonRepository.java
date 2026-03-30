package ulb.repository.json;

import ulb.repository.InventoryRepository;
import ulb.model.item.Inventory;

public class InventoryJsonRepository implements InventoryRepository {
	ItemJsonRepository itemRepository;

	public InventoryJsonRepository(ItemJsonRepository itemRepository) {
		this.itemRepository = itemRepository;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Inventory findStartingInventory() {
		return new Inventory(this.itemRepository.getStartingInventory());
	}
}
