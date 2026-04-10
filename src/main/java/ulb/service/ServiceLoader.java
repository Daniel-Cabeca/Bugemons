package ulb.service;

import ulb.model.bugemon.BugemonSpecies;
import ulb.repository.*;
import ulb.repository.database.AbilityDatabaseRepository;
import ulb.repository.database.AccountDatabaseRepository;
import ulb.repository.database.BugemonSpeciesDatabaseRepository;
import ulb.repository.database.ItemDatabaseRepository;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInitializer;
import ulb.repository.json.AbilityJsonRepository;
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
    private static AbilityService abilityService;
	private static AccountService accountService;

	static {
		// at class initialization
		loadDatabase();
	}

    public static BugemonService getBugemonService() {
        return bugemonService;
    }

    public static ItemService getItemService() {
        return itemService;
    }

    public static AbilityService getAbilityService() {
        return abilityService;
    }

	public static AccountService getAccountService() { return accountService; }

	/**
	 * Loads services with JSON repositories.
	 */
	private static void loadJson() {
		AbilityRepository abilityRepository = new AbilityJsonRepository();
		BugemonSpeciesRepository bugemonSpeciesRepository = new BugemonSpeciesJsonRepository();
		ItemJsonRepository itemRepository = new ItemJsonRepository();
		InventoryRepository inventoryRepository = new InventoryJsonRepository(itemRepository);

		ServiceLoader.bugemonService = new BugemonService(bugemonSpeciesRepository);
		ServiceLoader.itemService = new ItemService(itemRepository, inventoryRepository);
	}

	/**
	 * Loads services with database repositories.
   	 */
	private static void loadDatabase() {
		Database database = DatabaseInitializer.prepareDefaultDatabase();

		BugemonSpeciesRepository bugemonSpeciesRepository = new BugemonSpeciesDatabaseRepository(database);
		ItemRepository itemRepository = new ItemDatabaseRepository(database);
		InventoryRepository inventoryRepository = new InventoryJsonRepository(new ItemJsonRepository());
		AccountRepository accountRepository = new AccountDatabaseRepository(database);
		AbilityRepository abilityRepository = new AbilityJsonRepository();

		ServiceLoader.bugemonService = new BugemonService(bugemonSpeciesRepository);
		ServiceLoader.itemService = new ItemService(itemRepository, inventoryRepository);
		ServiceLoader.accountService = new AccountService(accountRepository);
		ServiceLoader.abilityService = new AbilityService(abilityRepository);
	}
}
