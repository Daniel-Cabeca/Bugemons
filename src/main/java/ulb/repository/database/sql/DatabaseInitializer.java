package ulb.repository.database.sql;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.ability.Ability;
import ulb.model.bugemon.BugemonSpecies;
import ulb.model.item.Item;
import ulb.repository.AbilityRepository;
import ulb.repository.ItemRepository;
import ulb.repository.database.AbilityDatabaseRepository;
import ulb.repository.database.BugemonSpeciesDatabaseRepository;
import ulb.repository.database.ItemDatabaseRepository;
import ulb.repository.json.AbilityJsonRepository;
import ulb.repository.json.BugemonSpeciesJsonRepository;
import ulb.repository.json.ItemJsonRepository;
import ulb.utils.DuplicateElementException;

import java.sql.SQLException;

/**
 * Creates and populates the tables of a database for the game.
 */
public record DatabaseInitializer(Database database) {
	private static final String SCRIPT_CREATE_TABLES = "/sql/init_db.sql";

	/**
	 * Creates a database initializer for a target database.
	 *
	 * @param database The database to initialize
	 */
	public DatabaseInitializer {
	}

	/**
	 * Prepares the default database.
	 *
	 * @return The default database, initialized and connected.
	 */
	public static Database prepareDefaultDatabase() throws LoadException, EntityNotFoundException {
		Database database = DatabaseInFile.get(DatabaseInFile.NAME_DEFAULT);
		DatabaseInitializer initializer = new DatabaseInitializer(database);

		if (database.isNew()) {
			initializer.initialize();
		}
		return database;
	}

	/**
	 * Creates the database, its tables and populates it with the default game data if it does not exist.
	 * The database's connection will be open after this call.
	 *
	 * @throws IllegalStateException If the database's connection is already open.
	 */
	public void initialize() throws LoadException, EntityNotFoundException {
		this.createTables();
		this.populate();
	}

	/**
	 * Creates the tables for the database.
	 */
	public void createTables() throws LoadException {
		SqlScript script = new SqlScript(SCRIPT_CREATE_TABLES);

		try {
			script.execute(this.database());
		} catch (SQLException e) {
			throw new LoadException("Failed to create tables for the database '" + this.database().getUrl() + "'.");
		}
	}

	/**
	 * Populates the database with the default game data.
	 */
	void populate() throws LoadException, EntityNotFoundException {
		ItemRepository itemRepository = new ItemJsonRepository();
		AbilityRepository abilityRepository = new AbilityJsonRepository();
		BugemonSpeciesJsonRepository bugemonSpeciesJsonRepository = new BugemonSpeciesJsonRepository();
		this.populate(itemRepository.findAll(), abilityRepository.findAll(), bugemonSpeciesJsonRepository.findAll());
	}

	/**
	 * Returns the database managed by this initializer.
	 *
	 * @return The managed database
	 */
	@Override
	public Database database() { return this.database; }

	/**
	 * Populates the database with game data.
	 *
	 * @param items The list of items
	 * @param abilities The list of abilities
	 * @param species The list of species
	 */
	void populate(Iterable<Item> items, Iterable<Ability> abilities, Iterable<BugemonSpecies> species) throws LoadException {
		ItemDatabaseRepository itemRepository = new ItemDatabaseRepository(this.database());
		AbilityDatabaseRepository abilityRepository = new AbilityDatabaseRepository(this.database());
		BugemonSpeciesDatabaseRepository bugemonSpeciesDatabaseRepository = new BugemonSpeciesDatabaseRepository(this.database);
		try {
			itemRepository.insertItems(items);
			abilityRepository.insertAbilities(abilities);
			bugemonSpeciesDatabaseRepository.insertSpecies(species);
		} catch (DuplicateElementException e) {
			throw new LoadException("The game data the database is populated with contains duplicate elements.");
		}
	}
}
