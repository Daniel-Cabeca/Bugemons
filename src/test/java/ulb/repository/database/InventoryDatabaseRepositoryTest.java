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

    private void insertUser(String username, Database database) throws LoadException {
        AccountDatabaseRepository accountRepository = new AccountDatabaseRepository(database);
        accountRepository.register(username, "password");
    }

    @Test
    public void testInsertItemAndGetInventory() throws DuplicateElementException, LoadException, NoSuchElementException {
        Database database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        ItemDatabaseRepository itemRepository = new ItemDatabaseRepository(database);
        InventoryDatabaseRepository inventoryRepository = new InventoryDatabaseRepository(database, itemRepository);

        String username = "testUsername";
        this.insertUser(username, database);

        Effect effect = new EffectHeal(EffectTarget.OPPOSITE_BUGEMON,5);

    	Item item1 = new Item("1","potion","","", effect, "");
    	Item item2 = new Item("2","potion2","","", effect, "");
        itemRepository.insertItem(item1);
        itemRepository.insertItem(item2);

        inventoryRepository.insertItem(item1, 1, username);
        inventoryRepository.insertItem(item2, 1, username);

        Inventory inventoryFromDatabase = inventoryRepository.getInventory(username);

        assertEquals(2, inventoryFromDatabase.getItems().size());
    }

    @Test
    public void testInsertInventoryAndGetInventory() throws DuplicateElementException, LoadException, NoSuchElementException {
        Database database = new DatabaseInMemory();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(database);
        databaseInitializer.createTables();

        ItemDatabaseRepository itemRepository = new ItemDatabaseRepository(database);
        InventoryDatabaseRepository inventoryRepository = new InventoryDatabaseRepository(database, itemRepository);

        String username = "testUsername";
        this.insertUser(username, database);

        Effect effect = new EffectHeal(EffectTarget.OPPOSITE_BUGEMON,5);

    	Item item1 = new Item("1","potion","","", effect, "");
    	Item item2 = new Item("2","potion2","","", effect, "");
        itemRepository.insertItem(item1);
        itemRepository.insertItem(item2);

        Inventory inventoryToAdd = new Inventory();
        inventoryToAdd.addItem(item1, 1);
        inventoryToAdd.addItem(item2, 1);

        inventoryRepository.insertInventory(inventoryToAdd, username);

        Inventory inventoryFromDatabase = inventoryRepository.getInventory(username);

        assertEquals(2, inventoryFromDatabase.getItems().size());
       
    }
}
