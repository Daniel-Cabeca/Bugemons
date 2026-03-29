package ulb.repository.mock;

import ulb.repository.InventoryRepository;
import ulb.repository.json.InventoryJsonRepository;
import ulb.repository.json.ItemJsonRepository;
import ulb.model.item.Inventory;

public class InventoryMockRepository implements InventoryRepository {
	private static InventoryRepository inventoryRepository = null;

	public InventoryMockRepository() {
		if (inventoryRepository == null) {
			load();
		}
	}

	private static void load() {
		inventoryRepository = new InventoryJsonRepository(new ItemJsonRepository());
	}

	public static void reload() {
		load();
	}

	@Override
	public Inventory findStartingInventory() {
		return inventoryRepository.findStartingInventory();
	}
}
