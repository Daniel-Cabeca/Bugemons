package ulb.repository.loader;

import java.lang.Iterable;

import ulb.model.item.Item;

/**
 * Loads a list of Item instances for the game data.
 */
public interface ItemLoader {
	/**
	 * Loads all items in the game data.
	 *
	 * @return An iterable object with all the loaded items
	 * @throws LoadFailureException If a runtime error occurs during the loading
	 */
	public Iterable<Item> loadAll() throws LoadFailureException;
}
