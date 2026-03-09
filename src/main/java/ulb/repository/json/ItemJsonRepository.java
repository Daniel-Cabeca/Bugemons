package ulb.repository.json;

import java.util.NoSuchElementException;

import ulb.repository.ItemRepository;
import ulb.repository.inmemory.ItemInMemoryRepository;
import ulb.model.item.Inventory;
import ulb.model.item.Item;

import ulb.repository.loader.LoadException;
import ulb.repository.loader.json.JsonResources;
import java.io.InputStream;
import ulb.repository.loader.ItemLoader;
import ulb.repository.loader.json.ItemJsonLoader;

/**
 * An item repository loaded from a json file.
 */
public class ItemJsonRepository implements ItemRepository {
	private final ItemRepository loadedItemRepository;

	/**
	 * Loads a repository from the default json files.
	 *
	 * @throws LoadException If loading failed
	 */
	public ItemJsonRepository() throws LoadException {
		String path = JsonResources.PATH_ITEMS;
		InputStream stream = JsonResources.getStream(path);
		ItemLoader loader = new ItemJsonLoader(stream);

		this.loadedItemRepository = new ItemInMemoryRepository(loader.loadAll());
	}

	@Override
	public Item findById(String id) throws NoSuchElementException {
		return this.loadedItemRepository.findById(id);
	}

	@Override
	public Iterable<Item> findAll() {
		return this.loadedItemRepository.findAll();
	}


	@Override
	public Inventory getStarterInventory() {
		return this.loadedItemRepository.getStarterInventory();
	}
}
