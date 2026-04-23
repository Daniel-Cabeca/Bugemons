package ulb.repository.mock;

import ulb.repository.StartingInventoryRepository;
import ulb.repository.json.StartingInventoryJsonRepository;
import ulb.repository.json.ItemJsonRepository;
import ulb.model.item.Inventory;

public class StartingInventoryMockRepository implements StartingInventoryRepository {
	private static StartingInventoryRepository inventoryRepository = null;

	public StartingInventoryMockRepository() {
		if (inventoryRepository == null) {
			load();
		}
	}

	private static void load() {
		inventoryRepository = new StartingInventoryJsonRepository(new ItemJsonRepository());
	}

	public static void reload() {
		load();
	}

	@Override
	public Inventory findStartingInventory() {
		return inventoryRepository.findStartingInventory();
	}
}
