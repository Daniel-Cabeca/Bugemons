package ulb.repository;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.item.Inventory;
import ulb.model.item.Item;

import java.util.NoSuchElementException;

public interface InventoryRepository {

	/**
	 * Saves an item to the inventory in the database.
	 *
	 * @param item the item that needs to be saved
	 * @param quantity the amount of the given item
	 * @param userId the owner of the inventory
	 * @throws LoadException
	 */
	public void insertItem(Item item, int quantity, int userId) throws LoadException;

	/**
	 * Deletes an item from the inventory in the database.
	 *
	 * @param item the item that needs to be deleted
	 * @param quantity the amount of the given item
	 * @param userId the owner of the inventory
	 * @throws LoadException
	 */
	public void deleteItem(Item item, int quantity, int userId) throws LoadException;

	/**
	 * Saves the inventory of the given user.
	 *
	 * @param inventory the inventory that needs to be saved
	 * @param userId the owner of the inventory
	 * @throws LoadException
	 */
	public void insertInventory(Inventory inventory, int userId) throws LoadException;

	/**
	 * Updates the inventory of the given user.
	 *
	 * @param inventory the updated inventory
	 * @param userId the owner of the inventory
	 * @throws LoadException
	 */
	public void updateInventory(Inventory inventory, int userId) throws LoadException;

	/**
	 * Loads the inventory of the given user.
	 *
	 * @param userId the user that requests it's inventory
	 * @return the corresponding inventory of the user
	 * @throws NoSuchElementException
	 */
	public Inventory getInventory(int userId) throws LoadException, EntityNotFoundException;
}
