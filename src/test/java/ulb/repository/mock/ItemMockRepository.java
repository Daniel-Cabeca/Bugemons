package ulb.repository.mock;

import ulb.exceptions.LoadException;

import java.util.NoSuchElementException;

import ulb.model.item.Item;
import ulb.repository.ItemRepository;
import ulb.repository.json.ItemJsonRepository;

/**
 * Mock repository for items.
 * Acts as an actual repository but is actually just using a static instance of a true repository loaded only once, for performance reasons.
 */
public class ItemMockRepository implements ItemRepository {
	private static ItemRepository itemRepository = null;
	private static ItemRepository mockData = null;

	public ItemMockRepository() {
		if (itemRepository == null) {
			load();
		}
	}

	private static void load() {
		try {
			itemRepository = new ItemJsonRepository();
			mockData = new ItemJsonRepository(MockResources.getStream(MockResources.PATH_ITEMS));
		} catch (LoadException e) {
			throw new IllegalStateException("Failed to load item mock data.", e);
		}
	}

	public static void reload() {
		load();
	}

	@Override
	public Item findById(String id) throws NoSuchElementException {
		try {
			return itemRepository.findById(id);
		} catch (NoSuchElementException e) {
			return mockData.findById(id);
		}
	}

	@Override
	public Iterable<Item> findAll() {
		return itemRepository.findAll();
	}
}
