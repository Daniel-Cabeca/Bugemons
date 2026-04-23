package ulb.repository;

import ulb.model.item.Inventory;

public interface StartingInventoryRepository {
    
    public Inventory findStartingInventory();
}
