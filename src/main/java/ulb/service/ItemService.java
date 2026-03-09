package ulb.service;

import java.util.NoSuchElementException;
import java.util.Map;

import ulb.repository.ItemRepository;
import ulb.model.item.Item;
import ulb.model.item.Inventory;
import ulb.model.bugemon.Bugemon;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
 
public class ItemService {
    private final ItemRepository itemRepository;
 


    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Fetches an Item by its id.
     *
     * @param id The item's id
     * @return The Item instance
     * @throws NoSuchElementException If no item matches the id
     */
    public Item getItem(String id) throws NoSuchElementException {
        return this.itemRepository.findById(id);
    }

    /**
     * Returns the list of all items.
     *
     * @return An iterable of all the items
     */
    public Iterable<Item> getAllItems() {
        return this.itemRepository.findAll();
    }

    /**
     * Creates the starter inventory for a new game, as defined in Histoire 10.
     * Contains:
     * - 3x Baie Revigorante
     * - 2x Baie Tonique
     * - 1x Gel Défensif
     * - 1x Sérum Offensif
     *
     * @return The starter Inventory
     */
    public Inventory createStarterInventory() {
        return this.itemRepository.getStarterInventory();
    }
    /**
     * Uses an item on a target Bugemon.
     *
     * @param item   The item to use
     * @param target The Bugemon to apply the item's effect on
     * @return 1 if the effect was applied successfully, 0 otherwise
     */
    public int useItem(Item item, Bugemon target) {
        return item.use(target);
    }


    public Item getRandomItem() {
        List<Item> items = new ArrayList<>();
        for (Item item : this.itemRepository.findAll()) {
            items.add(item);
        }
        Random random = new Random();
        return items.get(random.nextInt(items.size()));
    }

    public List<Item> getItemsByCategory(String category) {
        List<Item> result = new ArrayList<>();
        for (Item item : this.itemRepository.findAll()) {
            if (item.getCategory().equals(category)) {
                result.add(item);
            }
        }
        return result;
    }
}