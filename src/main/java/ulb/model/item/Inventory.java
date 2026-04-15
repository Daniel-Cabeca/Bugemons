package ulb.model.item;

import java.util.HashMap;
import java.util.Map;

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
	private Item findItemByID(String id) {
		for (Item item : items.keySet()) {
			if (item != null && item.getId().equals(id)) {
				return item;
			}
		}
		return null;
	}

	/**
	 * Adds an item to the inventory.
	 *
	 * @param item The item to add
	 * @param quantity The quantity to add
	 */
	public void addItem(Item item, int quantity) {
		Item itemToAdd = findItemByID(item.getId());

		if (itemToAdd == null) {
			this.items.put(item, quantity);
		} else {
			this.items.put(itemToAdd, this.items.get(itemToAdd) + quantity);
		}
	}

	/**
	 * Removes an item from the inventory.
	 *
	 * @param item The item to remove
	 */
	public void removeItem(Item item) {
		Item itemToRemove = findItemByID(item.getId());
		if (this.items.containsKey(itemToRemove)) {
			int current = this.items.get(itemToRemove);
			if (current <= 1) {
				this.items.remove(itemToRemove);
			} else {
				this.items.put(itemToRemove, current - 1);
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
		Item itemToFind = findItemByID(item.getId());
		Integer quantity = this.items.get(itemToFind);

		if (quantity == null) {
			return 0;
		}

		return quantity;
	}
}
