package ulb.repository.json;

import ulb.model.item.Inventory;
import ulb.repository.StartingInventoryRepository;

/**
 * Repository for the starting inventory specified in JSON files.
 * Requires an ItemJsonRepository as the starting inventory is given in the objets.json file.
 */
public class StartingInventoryJsonRepository implements StartingInventoryRepository {
	/** The repository holding all loaded items. */
	ItemJsonRepository itemRepository;

	public StartingInventoryJsonRepository(ItemJsonRepository itemRepository) {
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
