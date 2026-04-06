package ulb.service;

import ulb.repository.AbilityRepository;import ulb.repository.BugemonSpeciesRepository;import ulb.repository.InventoryRepository;import ulb.repository.ItemRepository;import ulb.repository.database.AbilityDatabaseRepository;import ulb.repository.database.BugemonSpeciesDatabaseRepository;import ulb.repository.database.ItemDatabaseRepository;import ulb.repository.database.sql.Database;import ulb.repository.database.sql.DatabaseInitializer;import ulb.repository.json.AbilityJsonRepository;
import ulb.repository.json.BugemonSpeciesJsonRepository;
import ulb.repository.json.ItemJsonRepository;
import ulb.repository.json.InventoryJsonRepository;

/**
 * Gives access to services.
 * Should eventually be deprecated ; services should be instanciated outside and injected into objects' constructors.
 */
public abstract class ServiceLoader {
    private static ItemService itemService;
	private static BugemonService bugemonService;

	static {
		// loads the services

		Database database = DatabaseInitializer.prepareDefaultDatabase();

		AbilityRepository abilityRepository = new AbilityDatabaseRepository(database);
		BugemonSpeciesRepository bugemonSpeciesRepository = new BugemonSpeciesDatabaseRepository(database);
		ItemRepository itemRepository = new ItemDatabaseRepository(database);

		InventoryRepository inventoryRepository = new InventoryJsonRepository(new ItemJsonRepository());

		ServiceLoader.bugemonService = new BugemonService(bugemonSpeciesRepository);
		ServiceLoader.itemService = new ItemService(itemRepository, inventoryRepository);
	}

	public static BugemonService getBugemonService() {
		return bugemonService;
	}

	public static ItemService getItemService() {
		return itemService;
	}
}
