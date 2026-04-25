package ulb.repository.database;

import org.junit.jupiter.api.Test;

import javafx.fxml.LoadException;
import ulb.model.effect.Effect;
import ulb.model.effect.EffectHeal;
import ulb.model.effect.EffectTarget;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInMemory;
import ulb.repository.database.sql.DatabaseInitializer;
import ulb.repository.database.sql.DatabaseMock;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.utils.DuplicateElementException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryDatabaseRepositoryTest {
    ItemDatabaseRepository itemRepository;
    InventoryDatabaseRepository inventoryRepository;
    String username;
    Item item1;
    Item item2;

    private void insertUser(String username, Database database) throws LoadException {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register(username, "password");
    }

    private void initiateDatabaseAndRepositories() throws LoadException, DuplicateElementException {
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
    public void testInsertItemAndGetInventory() throws DuplicateElementException, LoadException, NoSuchElementException {
        initiateDatabaseAndRepositories();

        this.inventoryRepository.insertItem(this.item1, 1, this.username);
        this.inventoryRepository.insertItem(this.item2, 1, this.username);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.username);

        assertEquals(2, inventoryFromDatabase.getItems().size());
    }

    @Test
    public void testInsertInventoryAndGetInventory() throws DuplicateElementException, LoadException, NoSuchElementException {
        initiateDatabaseAndRepositories();

        Inventory inventoryToAdd = new Inventory();
        inventoryToAdd.addItem(this.item1, 1);
        inventoryToAdd.addItem(this.item2, 1);

        this.inventoryRepository.insertInventory(inventoryToAdd, this.username);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.username);

        assertEquals(2, inventoryFromDatabase.getItems().size());
       
    }

    @Test 
    public void testDeleteItem() throws DuplicateElementException, LoadException, NoSuchElementException {
        initiateDatabaseAndRepositories();

        this.inventoryRepository.insertItem(this.item1, 1, this.username);
        this.inventoryRepository.insertItem(this.item2, 1, this.username);

        this.inventoryRepository.deleteItem(this.item2, 1, this.username);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.username);

        assertEquals(1, inventoryFromDatabase.getItems().size());
    }

    @Test
    public void updateInventory() throws DuplicateElementException, LoadException, NoSuchElementException {
        initiateDatabaseAndRepositories();
        
        this.inventoryRepository.insertItem(this.item1, 1, this.username);

        Inventory updatedInventory = new Inventory();
        updatedInventory.addItem(this.item1, 2);
        updatedInventory.addItem(item2, 1);

        this.inventoryRepository.updateInventory(updatedInventory, this.username);

        Inventory inventoryFromDatabase = this.inventoryRepository.getInventory(this.username);

        assertEquals(2, inventoryFromDatabase.getItems().size());
        assertEquals(2, inventoryFromDatabase.getQuantity(item1));
    }
}
