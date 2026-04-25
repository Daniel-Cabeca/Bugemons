package ulb.repository.database.sql;

import java.sql.SQLException;

import ulb.model.ability.Ability;
import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.AbilityRepository;
import ulb.repository.database.BugemonSpeciesDatabaseRepository;
import ulb.repository.database.ItemDatabaseRepository;
import ulb.repository.database.AbilityDatabaseRepository;
import ulb.repository.json.AbilityJsonRepository;
import ulb.repository.json.BugemonSpeciesJsonRepository;
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

	/**
	 * Creates a database initializer for a target database.
	 *
	 * @param database The database to initialize
	 */
	public DatabaseInitializer(Database database) {
		this.database = database;
	}

	/**
	 * Returns the database managed by this initializer.
	 *
	 * @return The managed database
	 */
	public Database getDatabase() { return this.database; }

	/**
	 * Creates the tables for the database.
	 */
	public void createTables() {
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
	 * @param abilities The list of abilities
	 * @param species The list of species
	 */
	void populate(Iterable<Item> items, Iterable<Ability> abilities, Iterable<BugemonSpecies> species) {
		ItemDatabaseRepository itemRepository = new ItemDatabaseRepository(this.getDatabase());
		AbilityDatabaseRepository abilityRepository = new AbilityDatabaseRepository(this.getDatabase());
		BugemonSpeciesDatabaseRepository bugemonSpeciesDatabaseRepository = new BugemonSpeciesDatabaseRepository(this.database);
		try {
			itemRepository.insertItems(items);
			abilityRepository.insertAbilities(abilities);
			bugemonSpeciesDatabaseRepository.insertSpecies(species);
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
		BugemonSpeciesJsonRepository bugemonSpeciesJsonRepository = new BugemonSpeciesJsonRepository();
		this.populate(itemRepository.findAll(),abilityRepository.findAll(),bugemonSpeciesJsonRepository.findAll());
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
