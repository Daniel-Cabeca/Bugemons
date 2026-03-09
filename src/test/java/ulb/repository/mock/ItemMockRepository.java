package ulb.repository.mock;

import java.util.NoSuchElementException;

import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.repository.ItemRepository;
import ulb.repository.json.ItemJsonRepository;

/**
 * Mock repository for items.
 * Acts as an actual repository but is actually just using a static instance of a true repository loaded only once, for performance reasons.
 */
public class ItemMockRepository implements ItemRepository {
	private static ItemRepository itemRepository = null;

	public ItemMockRepository() {
		if (itemRepository == null) {
			load();
		}
	}

	private static void load() {
		itemRepository = new ItemJsonRepository();
	}

	public static void reload() {
		load();
	}

	@Override
	public Item findById(String id) throws NoSuchElementException {
		return itemRepository.findById(id);
	}

	@Override
	public Iterable<Item> findAll() {
		return itemRepository.findAll();
	}


    @Override
	public Inventory getStarterInventory() {
		return itemRepository.getStarterInventory();
	}
}
