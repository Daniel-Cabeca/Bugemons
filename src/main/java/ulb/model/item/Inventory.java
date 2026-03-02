package ulb.model.item;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
	private Map<Item, Integer> items;

	public Inventory() {
		this.items = new HashMap<>();
	}

	public void addItem(Item item, int quantity) {
		this.items.put(item, this.items.getOrDefault(item, 0) + quantity);
	}

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
}
