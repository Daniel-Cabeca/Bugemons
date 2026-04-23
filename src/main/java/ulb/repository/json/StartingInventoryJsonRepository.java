package ulb.repository.json;

import ulb.repository.InventoryRepository;
import ulb.repository.StartingInventoryRepository;
import ulb.model.item.Inventory;

// /**
//  * Repository for the starting inventory specified in JSON files.
//  * Requires an ItemJsonRepository as the starting inventory is given in the objets.json file.
//  */
public class StartingInventoryJsonRepository implements StartingInventoryRepository {
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



// /**
//  * Repository for the starting inventory specified in JSON files.
//  * Requires an ItemJsonRepository as the starting inventory is given in the objets.json file.
//  */
// public class InventoryJsonRepository implements InventoryRepository {
// 	ItemJsonRepository itemRepository;

// 	public InventoryJsonRepository(ItemJsonRepository itemRepository) {
// 		this.itemRepository = itemRepository;
// 	}

// 	/**
// 	 * {@inheritDoc}
// 	 */
// 	@Override
// 	public Inventory findStartingInventory() {
// 		return new Inventory(this.itemRepository.getStartingInventory());
// 	}
// }
// }
