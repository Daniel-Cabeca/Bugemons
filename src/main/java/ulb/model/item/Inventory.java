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
	 * Adds an item to the inventory.
	 *
	 * @param item The item to add
	 * @param quantity The quantity to add
	 */
	public void addItem(Item item, int quantity) {
		this.items.put(item, this.items.getOrDefault(item, 0) + quantity);
	}

	/**
	 * Removes an item from the inventory.
	 *
	 * @param item The item to remove
	 */
	public void removeItem(Item item) {
		if (this.items.containsKey(item)) {
			int current = this.items.get(item);
			if (current <= 1) {
				this.items.remove(item);
			} else {
				this.items.put(item, current - 1);
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
		Integer quantity = this.items.get(item);

		if (quantity == null) {
			return 0;
		}

		return quantity;
	}
}
