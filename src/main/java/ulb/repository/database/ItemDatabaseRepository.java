package ulb.repository.database;

import ulb.model.bugemon.Stats;
import ulb.model.effect.Effect;
import ulb.model.effect.EffectHeal;
import ulb.model.effect.EffectStatModifier;
import ulb.model.item.Item;
import ulb.repository.ItemRepository;
import ulb.repository.LoadException;
import ulb.repository.json.ItemJsonRepository;
import ulb.utils.DuplicateElementException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

/**
 * Items repository connected to the SLQ database.
 */
public class ItemDatabaseRepository implements ItemRepository {
	private final Database database;

	public ItemDatabaseRepository(Database database) throws LoadException {
		this.database = database;
	}

	/**
	 * Inserts items in the database.
	 *
	 * @param items The items to insert
	 */
	public void insertItems(Iterable<Item> items) throws DuplicateElementException {
		for (Item item: items) {
			this.insertItem(item);
		}
	}

	/**
	 * Inserts an item in the database.
	 *
	 * @param item The item to insert
	 */
	public void insertItem(Item item) throws DuplicateElementException {
		String sql = "INSERT INTO items (id, name, description, category, sprite) VALUES (?, ?, ?, ?, ?)";

		try (PreparedStatement statement = this.database.prepareStatement(sql)) {
			statement.setString(1, item.getId());
			statement.setString(2, item.getName());
			statement.setString(3, item.getDescription());
			statement.setString(4, item.getCategory());
			statement.setString(5, item.getSprite());

			statement.executeUpdate();

			EffectLoader effectloader = new EffectLoader(this.database);
			effectloader.insert(item.getEffect(), item.getId());
		} catch (SQLException e) {
			throw new DuplicateElementException("Failed to insert item: "+ item.getId() +" ("+ e.getMessage() +")");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item findById(String id) throws NoSuchElementException {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Item> findAll() {
		return null;
	}
}
