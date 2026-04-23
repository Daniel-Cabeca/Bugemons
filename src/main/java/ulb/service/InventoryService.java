package ulb.service;

import java.util.NoSuchElementException;

import ulb.repository.ItemRepository;
import ulb.repository.InventoryRepository;
import ulb.model.item.Item;
import ulb.model.item.Inventory;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class InventoryService {
	private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // /**
	// * Creates the starter inventory for a new game, as defined in Histoire 10.
	// * Contains:
	// * - 3x Baie Revigorante
	// * - 2x Baie Tonique
	// * - 1x Gel Défensif
	// * - 1x Sérum Offensif
	// *
	// * @return The starter Inventory
	// */
	// public Inventory createStarterInventory(String username) {
    //     Inventory starterInventory = this.inventoryRepository.starterInventory();
    //     this.insertInventory(starterInventory, username);
	// 	return starterInventory;
	// }

    public void insertInventory(Inventory inventory, String username) {
        this.inventoryRepository.insertInventory(inventory, username);
    }

    public void insertItem(Item item, int quantity, String username) {
        this.inventoryRepository.insertItem(item, quantity, username);
    }
}
