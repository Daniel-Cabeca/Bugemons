package ulb.repository.database;

import ulb.model.item.Inventory;
import ulb.model.item.Item;
import ulb.repository.InventoryRepository;
import ulb.repository.ItemRepository;
import ulb.exceptions.LoadException;
import ulb.repository.database.sql.Database;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Inventory repository connected to the database
 */
public class InventoryDatabaseRepository implements InventoryRepository {
	private static final Logger LOGGER = Logger.getLogger(InventoryDatabaseRepository.class.getName());

    private final Database database;
    private ItemRepository itemRepository;

    /**
     * Creates an inventory repository using the given database
     * @param database the database used
     * @param itemRepository used to acces the items in the database
     * @throws LoadException
     */
	public InventoryDatabaseRepository(Database database, ItemRepository itemRepository) throws LoadException {
		this.database = database;
        this.itemRepository = itemRepository;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertItem(Item item, int quantity, String username) throws LoadException {
        String sql = """
                    INSERT INTO inventory (user_id, item_id, quantity)
                    VALUES ((SELECT id FROM users WHERE username = ?), ?, ?)
                    ON CONFLICT(user_id, item_id)
                    DO UPDATE SET quantity = quantity + excluded.quantity
                """;
        try (PreparedStatement stmt = this.database.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, item.getId());
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to insert item to inventory: " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void insertInventory(Inventory inventory, String username) throws LoadException {
        for (Item item : inventory.getItems().keySet()) {
            int quantity = inventory.getQuantity(item);
            this.insertItem(item, quantity, username);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override 
    public void deleteItem(Item item, int quantity, String username) throws LoadException {
        String sql1 = """
                UPDATE inventory
                SET quantity = quantity - ?
                WHERE user_id = (SELECT id FROM users WHERE username = ?) 
                AND item_id = ?
                """;
        try (PreparedStatement stmt = this.database.prepareStatement(sql1)) {
            stmt.setInt(1, quantity);
            stmt.setString(2, username);
            stmt.setString(3, item.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to delete item from inventory: " + e.getMessage());
        }

        String sql2 = """
                DELETE FROM inventory
                WHERE quantity <= 0
                AND user_id = (SELECT id FROM users WHERE username = ?)
                """;
        try (PreparedStatement stmt = this.database.prepareStatement(sql2)) {
            stmt.setString(1, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new LoadException("Failed to clean inventory: " + e.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInventory(Inventory inventory, String username) throws LoadException {
        try {
            Connection conn = database.getConnection();
            conn.setAutoCommit(false);

            String sql = """
                    DELETE FROM inventory
                    WHERE user_id = (SELECT id FROM users WHERE username = ?)
                    """;

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.executeUpdate();
            }

            this.insertInventory(inventory, username);

            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            try {
                database.getConnection().rollback();
            } catch (SQLException ignored) {}
            throw new LoadException("Failed to update inventory" + e);
        }
    }

    /**
     * {@inheritDoc}
     */
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
                Item item = this.itemRepository.findById(itemID);
                int quantity = rs.getInt("quantity");

				inventory.addItem(item, quantity);
			}
		} catch (SQLException e) {
			LOGGER.log(Level.WARNING, "Failed to load inventory for user: " + username, e);
		}

        return inventory;
    }
}
