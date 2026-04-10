package ulb.repository.database;

import ulb.model.bugemon.Stats;
import ulb.model.effect.*;
import ulb.model.item.Item;
import ulb.repository.ItemRepository;
import ulb.repository.LoadException;
import ulb.repository.database.sql.Database;
import ulb.repository.json.ItemJsonRepository;
import ulb.utils.DuplicateElementException;

import java.sql.*;
import java.util.*;

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

			EffectDatabaseRepository effectRepository = new EffectDatabaseRepository(this.database);
			effectRepository.insert(item.getEffect(), item.getId(),true);
		} catch (SQLException e) {
			throw new DuplicateElementException("Failed to insert item: "+ item.getId() +" ("+ e.getMessage() +")");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item findById(String id) {
		// La grosse requête qui rassemble tout (Outer Join pour ne rien perdre)
		String sql = """
        SELECT i.*, e.id AS effect_id, e.type AS effect_type, e.target, e.value, 
               esm.hp, esm.attack, esm.defense, esm.initiative, esm.duration
        FROM items i
        LEFT JOIN effects e ON i.id = e.item_id
        LEFT JOIN effect_stats_modifier esm ON e.id = esm.effect_id
        WHERE i.id = ?
    """;

		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			pstmt.setString(1, id);

			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				// 1. On crée l'effet selon son type
				Effect effect = null;
				String typeStr = rs.getString("effect_type");

				if (typeStr != null) {
					EffectType type = EffectType.valueOf(typeStr);
					EffectTarget target = EffectTarget.valueOf(rs.getString("target"));
					if (type == EffectType.HEAL) {
						effect = new EffectHeal(target, rs.getInt("value"));
					}
					else if (type == EffectType.STAT_MODIFIER) {
						// On reconstruit l'objet Stats du modificateur
						Map<EffectStatType, Integer> statsChanges = new EnumMap<>(EffectStatType.class);
						statsChanges.put(EffectStatType.HP, rs.getInt("hp"));
						statsChanges.put(EffectStatType.ATTACK, rs.getInt("attack"));
						statsChanges.put(EffectStatType.DEFENSE, rs.getInt("defense"));
						statsChanges.put(EffectStatType.INITIATIVE, rs.getInt("initiative"));

						String durationStr = rs.getString("duration");
						EffectStatDuration duration = EffectStatDuration.valueOf(durationStr);
						effect = new EffectStatModifier(target,duration,statsChanges);
					}
				}

				// 2. On retourne l'Item complet
				return new Item(
						rs.getString("id"),
						rs.getString("name"),
						rs.getString("description"),
						rs.getString("category"),
						effect,
						rs.getString("sprite")
				);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<Item> findAll() {
		List<Item> items = new ArrayList<>();
		String sql = "SELECT id FROM items";

		// On récupère d'abord tous les IDs
		try (PreparedStatement pstmt = this.database.prepareStatement(sql)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String itemId = rs.getString("id");
				try {
					// On réutilise ta méthode findById pour charger l'objet complet
					Item item = findById(itemId);
					if (item != null) {
						items.add(item);
					}
				} catch (NoSuchElementException e) {
					// Si un ID existe mais que findById échoue (peu probable mais possible)
					System.err.println("Erreur : ID trouvé mais item non chargeable : " + itemId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return items;
	}
}
