package ulb.service;

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

    static {
        // loads the services

        AbilityJsonRepository abilityRepository = new AbilityJsonRepository();
        BugemonSpeciesJsonRepository bugemonSpeciesRepository = new BugemonSpeciesJsonRepository(abilityRepository);
        ItemJsonRepository itemRepository = new ItemJsonRepository();
        InventoryJsonRepository inventoryRepository = new InventoryJsonRepository(itemRepository);

        ServiceLoader.abilityService = new AbilityService(abilityRepository);
        ServiceLoader.bugemonService = new BugemonService(bugemonSpeciesRepository);
        ServiceLoader.itemService = new ItemService(itemRepository, inventoryRepository);
    }

    public static BugemonService getBugemonService() {
        return bugemonService;
    }

    public static ItemService getItemService() {
        return itemService;
    }

    public static AbilityService getAbilityService(){
        return abilityService;
    }
}
