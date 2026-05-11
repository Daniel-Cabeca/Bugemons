package ulb.model.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * An inventory containing variable quantities of items.
 */
public class Inventory {
	private Map<Item, Integer> items;

	public Inventory() {
		this.items = new HashMap<>();
	}

	public Inventory(Inventory o) {
		this.items = new HashMap<>(o.items);
	}

	/**
	 * Looks for an item with the given id.
	 * @param id the id of the item to find
	 * @return the coresponding item if found, if not returns null
	 */
	private Optional<Item> findItemByID(String id) {
		for (Item item : items.keySet()) {
			if (item != null && item.getId().equals(id)) {
				return Optional.of(item);
			}
		}
		return Optional.empty();
	}

	/**
	 * Adds an item to the inventory.
	 *
	 * @param item The item to add
	 * @param quantity The quantity to add
	 */
	public void addItem(Item item, int quantity) {
		Optional<Item> itemToAdd = findItemByID(item.getId());

		if (itemToAdd.isEmpty()) {
			this.items.put(item, quantity);
		} else {
			this.items.put(itemToAdd.get(), this.items.get(itemToAdd.get()) + quantity);
		}
	}

	/**
	 * Removes an item from the inventory.
	 *
	 * @param item The item to remove
	 */
	public void removeItem(Item item) {
		Optional<Item> itemToRemove = findItemByID(item.getId());
		if (itemToRemove.isPresent() && this.items.containsKey(itemToRemove.get())) {
			int current = this.items.get(itemToRemove.get());
			if (current <= 1) {
				this.items.remove(itemToRemove.get());
			} else {
				this.items.put(itemToRemove.get(), current - 1);
			}
		}
	}

	public Map<Item, Integer> getItems() {
		return this.items;
	}

	/**
	 * Returns the quantity of an item in the inventory.
	 *
	 * @return The quantity of the item in the inventory (0 if not present)
	 */
	public int getQuantity(Item item) {
		Optional<Item> itemToFind = findItemByID(item.getId());
		if (itemToFind.isEmpty()){
			return 0;
		}

		Integer quantity = this.items.get(itemToFind.get());

		if (quantity == null) {
			return 0;
		}

		return quantity;
	}
}
