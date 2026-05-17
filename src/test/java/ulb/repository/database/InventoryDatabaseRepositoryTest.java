package ulb.repository.database;

import org.junit.jupiter.api.Test;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.exceptions.UserAlreadyExistsException;
import ulb.model.effect.Effect;
import ulb.model.effect.EffectHeal;
import ulb.model.effect.EffectTarget;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryDatabaseRepositoryTest {
    ItemDatabaseRepository itemRepository;
    InventoryDatabaseRepository inventoryRepository;
    String username;
	int userId;
    Item item1;
    Item item2;

    private void insertUser(String username, Database database) throws LoadException, EntityNotFoundException, UserAlreadyExistsException {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register(username, "password");
		this.userId = accountRepository.getUserId(username);
    }

    private void initiateDatabaseAndRepositories() throws Exception {
        Database database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        this.itemRepository = new ItemDatabaseRepository(database);
        this.inventoryRepository = new InventoryDatabaseRepository(database, itemRepository);

        this.username = "testUsername";
        this.insertUser(username, database);

        Effect effect = new EffectHeal(EffectTarget.OPPOSITE_BUGEMON,5);

    	this.item1 = new Item("1","potion","","", effect, "");
    	this.item2 = new Item("2","potion2","","", effect, "");
        this.itemRepository.insertItem(item1);
        this.itemRepository.insertItem(item2);
    }

    @Test
    public void testInsertItemAndGetInventory() throws Exception {
        initiateDatabaseAndRepositories();

        this.inventoryRepository.insertItem(this.item1, 1, this.userId);
        this.inventoryRepository.insertItem(this.item2, 1, this.userId);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.userId);

        assertEquals(2, inventoryFromDatabase.getItems().size());
    }

    @Test
    public void testInsertInventoryAndGetInventory() throws Exception {
        initiateDatabaseAndRepositories();

        Inventory inventoryToAdd = new Inventory();
        inventoryToAdd.addItem(this.item1, 1);
        inventoryToAdd.addItem(this.item2, 1);

        this.inventoryRepository.insertInventory(inventoryToAdd, this.userId);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.userId);

        assertEquals(2, inventoryFromDatabase.getItems().size());
       
    }

    @Test 
    public void testDeleteItem() throws Exception {
        initiateDatabaseAndRepositories();

        this.inventoryRepository.insertItem(this.item1, 1, this.userId);
        this.inventoryRepository.insertItem(this.item2, 1, this.userId);

        this.inventoryRepository.deleteItem(this.item2, 1, this.userId);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.userId);

        assertEquals(1, inventoryFromDatabase.getItems().size());
    }

    @Test
    public void updateInventory() throws Exception {
        initiateDatabaseAndRepositories();
        
        this.inventoryRepository.insertItem(this.item1, 1, this.userId);

        Inventory updatedInventory = new Inventory();
        updatedInventory.addItem(this.item1, 2);
        updatedInventory.addItem(item2, 1);

        this.inventoryRepository.updateInventory(updatedInventory, this.userId);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.userId);

        assertEquals(2, inventoryFromDatabase.getItems().size());
        assertEquals(2, inventoryFromDatabase.getQuantity(item1));
    }
}
