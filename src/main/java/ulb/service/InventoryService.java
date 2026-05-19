package ulb.service;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.Player;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.repository.InventoryRepository;

import java.util.NoSuchElementException;

/**
 * Service layer for inventory saving and loading.
 */
public class InventoryService {
	private final InventoryRepository inventoryRepository;

	/**
	 * Creates an inventory service using the provided repository.
	 *
	 * @param inventoryRepository the repository that is used
	 */
	public InventoryService(InventoryRepository inventoryRepository) {
		this.inventoryRepository = inventoryRepository;
	}

	/**
	 * Saves given inventory of the given user to the database.
	 *
	 * @param inventory the inventory that needs to be saved to the database
	 * @param userId the owner of the inventory that needs to be saved to the database
	 * @throws LoadException
	 */
	public void insertInventory(Inventory inventory, int userId) throws LoadException {
		this.inventoryRepository.insertInventory(inventory, userId);
	}

	/**
	 * Inserts given item to the inventory of the given user.
	 *
	 * @param item the item to save to the inventory
	 * @param quantity the amount of times the item is given
	 * @param player the owner of the inventory
	 * @throws LoadException
	 */
	public void insertItem(Item item, int quantity, Player player) throws LoadException {
		if (player.getUserId().isPresent()) {
			this.inventoryRepository.insertItem(item, quantity, player.getUserId().get());
		}
	}

	/**
	 * Deletes given item from the inventory of the given user.
	 *
	 * @param item the item that needs to be removed from the inventory
	 * @param player the owner of the inventory
	 * @throws LoadException
	 */
	public void deleteItem(Item item, int quantity, Player player) throws LoadException {
		if (player.getUserId().isPresent()) {
			this.inventoryRepository.deleteItem(item, quantity, player.getUserId().get());
		}
	}

	/**
	 * Updates the inventory of the given user.
	 *
	 * @param inventory the updated inventory
	 * @param player the owner of the inventory
	 * @throws LoadException
	 */
	public void updateInventory(Inventory inventory, Player player) throws LoadException {
		if (player.getUserId().isPresent()) {
			this.inventoryRepository.updateInventory(inventory, player.getUserId().get());
		}
	}

	/**
	 * Gets the saved inventory of the given user from the database.
	 *
	 * @param userId the owner of the inventory
	 * @return the inventory of the given user
	 * @throws NoSuchElementException
	 */
	public Inventory getInventoryFromDatabase(int userId) throws LoadException, EntityNotFoundException {
		return this.inventoryRepository.getInventory(userId);
	}
}
