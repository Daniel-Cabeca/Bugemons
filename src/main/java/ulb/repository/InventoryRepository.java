package ulb.repository;

import java.util.NoSuchElementException;

import ulb.exceptions.LoadException;
import ulb.model.item.Inventory;
import ulb.model.item.Item;

public interface InventoryRepository {

	/**
	 * Saves an item to the inventory in the database.
	 * @param item the item that needs to be saved
	 * @param quantity the amount of the given item
	 * @param username the owner of the inventory
	 * @throws LoadException
	 */
	public void insertItem(Item item, int quantity, String username) throws LoadException;

	/**
	 * Deletes an item from the inventory in the database.
	 * @param item the item that needs to be deleted
	 * @param quantity the amount of the given item
	 * @param username the owner of the inventory
	 * @throws LoadException
	 */
	public void deleteItem(Item item, int quantity, String username) throws LoadException;

	/**
	 * Saves the inventory of the given user.
	 * @param inventory the inventory that needs to be saved
	 * @param username the owner of the inventory
	 * @throws LoadException
	 */
	public void insertInventory(Inventory inventory, String username) throws LoadException;

	/**
	 * Updates the inventory of the given user.
	 * @param inventory the updated inventory
	 * @param username the owner of the inventory
	 * @throws LoadException
	 */
	public void updateInventory(Inventory inventory, String username) throws LoadException;

	/**
	 * Loads the inventory of the given user.
	 * @param username the user that requests it's inventory
	 * @return the corresponding inventory of the user
	 * @throws NoSuchElementException
	 */
	public Inventory getInventory(String username) throws NoSuchElementException;
}
