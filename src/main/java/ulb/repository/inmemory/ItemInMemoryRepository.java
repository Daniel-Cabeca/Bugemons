package ulb.repository.inmemory;

import java.util.NoSuchElementException;

import ulb.repository.ItemRepository;
import ulb.model.item.Item;

public class ItemInMemoryRepository implements ItemRepository {
	private final IdSet<Item> items = new IdSet<>();

	/**
	 * Creates an empty item repository.
	 */
	public ItemInMemoryRepository() {
	}

	/**
	 * Creates a pre-filled item repository.
	 *
	 * @param items The items to fill the repository with.
	 * @throws IllegalArgumentException If trying to add duplicate items.
	 */
	public ItemInMemoryRepository(Iterable<Item> items) throws IllegalArgumentException {
		this.add(items);
	}

	/**
	 * Adds items to the repository.
	 *
	 * @param items The items to add.
	 * @throws IllegalArgumentException If trying to add duplicate items.
	 */
	public void add(Iterable<Item> items) throws IllegalArgumentException {
		for (Item item: items) {
			this.items.add(item);
		}
	}

	@Override
	public Item findById(String id) throws NoSuchElementException {
		return this.items.get(id);
	}

	@Override
	public Iterable<Item> findAll() {
		return this.items;
	}
}
