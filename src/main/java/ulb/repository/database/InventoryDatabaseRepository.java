package ulb.repository.database;

import ulb.model.effect.*;
import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.model.team.Team;
import ulb.repository.InventoryRepository;
import ulb.repository.ItemRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;
import ulb.utils.DuplicateElementException;

import java.sql.*;
import java.util.*;

public class InventoryDatabaseRepository implements InventoryRepository {
    private final Database database;
    private ItemRepository itemRepository;

	public InventoryDatabaseRepository(Database database, ItemRepository itemRepository) throws LoadException {
		this.database = database;
        this.itemRepository = itemRepository;
	}

    @Override
    public void insertItem(Item item, int quantity, String username) throws LoadException {
        String sql = "INSERT INTO inventory (user_id, item_id, quantity) VALUES ((SELECT id FROM users WHERE username = ?), ?, ?)";
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, item.getId());
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to insert item to inventory: " + e.getMessage());
        }
    }

    @Override
    public void insertInventory(Inventory inventory, String username) throws LoadException {
        for (Item item : inventory.getItems().keySet()) {
            int quantity = inventory.getQuantity(item);
            this.insertItem(item, quantity, username);
        }
    }

    @Override
    public Inventory getInventory(String username) throws NoSuchElementException {
        Inventory inventory = new Inventory();

        String sql = """
        SELECT i.id, inv.quantity
        FROM items i
        JOIN inventory inv ON i.id = inv.item_id
        JOIN users u ON u.id = inv.user_id
        WHERE u.username = ?
        """;

        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
                String itemID = rs.getString("id");
                Item item = itemRepository.findById(itemID);
                int quantity = rs.getInt("quantity");

				inventory.addItem(item, quantity);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

        return inventory;
    }
}
