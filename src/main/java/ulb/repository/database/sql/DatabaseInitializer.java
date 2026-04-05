package ulb.repository.database.sql;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import ulb.model.ability.Ability;
import ulb.repository.AbilityRepository;
import ulb.repository.database.ItemDatabaseRepository;
import ulb.repository.database.abilityDatabaseRepository;
import ulb.repository.json.AbilityJsonRepository;
import ulb.utils.DuplicateElementException;

import ulb.model.item.Item;
import ulb.repository.ItemRepository;
import ulb.repository.json.ItemJsonRepository;

/**
 * Creates and populates the tables of a database for the game.
 */
public class DatabaseInitializer {
	private static final String SCRIPT_CREATE_TABLES = "/sql/init_db.sql";

	private final Database database;

	public DatabaseInitializer(Database database) {
		this.database = database;
	}

	public Database getDatabase() { return this.database; }

	/**
	 * Creates the tables for the database.
	 */
	void createTables() {
		SqlScript script = new SqlScript(SCRIPT_CREATE_TABLES);

		try {
			script.execute(this.getDatabase());
		} catch (SQLException e) {
			throw new RuntimeException("Failed to create tables for the database '"+ this.getDatabase().getUrl() +"': "+ e.getMessage());
		}
	}

	/**
	 * Populates the database with game data.
	 *
	 * @param items The list of items
	 */
	void populate(Iterable<Item> items, Iterable<Ability> abilities) {
		ItemDatabaseRepository itemRepository = new ItemDatabaseRepository(this.getDatabase());
		abilityDatabaseRepository abilityRepository = new abilityDatabaseRepository(this.getDatabase());
		try {
			itemRepository.insertItems(items);
			abilityRepository.insertAbilities(abilities);
		} catch(DuplicateElementException e) {
			throw new RuntimeException("The game data the database is populated with contains duplicate elements.");
		}
	}

	/**
	 * Populates the database with the default game data.
	 */
	void populate() {
		ItemRepository itemRepository = new ItemJsonRepository();
		AbilityRepository abilityRepository = new AbilityJsonRepository();

		this.populate(itemRepository.findAll(),abilityRepository.findAll());
	}

	/**
	 * Creates the database, its tables and populates it with the default game data if it does not exist.
	 * The database's connection will be open after this call.
	 *
	 * @throws IllegalStateException If the database's connection is already open.
	 */
	public void initialize() {
		this.createTables();
		this.populate();
	}

	/**
	 * Prepares the default database.
	 *
	 * @return The default database, initialized and connected.
	 */
	public static Database prepareDefaultDatabase() {
		Database database = DatabaseInFile.get(DatabaseInFile.NAME_DEFAULT);
		DatabaseInitializer initializer = new DatabaseInitializer(database);

		if (database.isNew()) {
			initializer.initialize();
		}
		return database;
	}
}
