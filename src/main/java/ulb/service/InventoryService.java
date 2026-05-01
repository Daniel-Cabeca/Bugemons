package ulb.service;

import ulb.model.item.Item;
import ulb.model.item.Inventory;
import ulb.repository.InventoryRepository;
import ulb.exceptions.LoadException;

import java.util.NoSuchElementException;

/**
 * Service layer for inventory saving and loading.
 */
public class InventoryService {
	private final InventoryRepository inventoryRepository;

    /**
     * Creates an inventory service using the provided repository.
     * @param inventoryRepository the repository that is used
     */
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    /**
     * Saves given inventory of the given user to the database.
     * @param inventory the inventory that needs to be saved to the database
     * @param username the owner of the inventory that needs to be saved to the database
     * @throws LoadException
     */
    public void insertInventory(Inventory inventory, String username) throws LoadException {
        this.inventoryRepository.insertInventory(inventory, username);
    }

    /**
     * Inserts given item to the inventory of the given user.
     * @param item the item to save to the inventory
     * @param quantity the amount of times the item is given
     * @param username the owner of the inventory
     * @throws LoadException
     */
    public void insertItem(Item item, int quantity, String username) throws LoadException {
        this.inventoryRepository.insertItem(item, quantity, username);
    }

    /**
     * Deletes given item from the inventory of the given user.
     * @param item the item that needs to be removed from the inventory
     * @param username the owner of the inventory
     * @throws LoadException
     */
    public void deleteItem(Item item, int quantity, String username) throws LoadException {
        this.inventoryRepository.deleteItem(item, quantity, username);
    }

    /**
     * Updates the inventory of the given user.
     * @param inventory the updated inventory
     * @param username the owner of the inventory
     * @throws LoadException
     */
    public void updateInventory(Inventory inventory, String username) throws LoadException {
        this.inventoryRepository.updateInventory(inventory, username);
    }

    /**
     * Gets the saved inventory of the given user from the database.
     * @param username the owner of the inventory
     * @return the inventory of the given user
     * @throws NoSuchElementException
     */
    public Inventory getInventoryFromDatabase(String username) throws NoSuchElementException {
        return this.inventoryRepository.getInventory(username);
    }
}
