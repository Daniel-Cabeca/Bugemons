package ulb.repository;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.item.Item;

/**
 * Repository of the Items instances in the game data.
 */
public interface ItemRepository {
	/**
	 * Gets an Item instance for the corresponding id.
	 *
	 * @param id The id of the item
	 * @return The Item instance
	 * @throws EntityNotFoundException If no match was found for the id
	 */
	Item findById(String id) throws EntityNotFoundException;

	/**
	 * Gives an iterable list of the items in the repository.
	 *
	 * @return An object iterable over the existing items
	 */
	Iterable<Item> findAll() throws LoadException, EntityNotFoundException;

}
