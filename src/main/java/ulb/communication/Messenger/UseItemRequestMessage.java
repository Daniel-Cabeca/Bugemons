package ulb.communication.Messenger;

import ulb.communication.Message;
import ulb.model.item.Item;

/**
 * Player chose to use an item in manual battle. {@link Item} is not serialized (transient).
 */
public class UseItemRequestMessage implements Message {
	private final transient Item item;

	/**
	 * @param item the inventory item the player selected (transient reference)
	 */
	public UseItemRequestMessage(Item item) {
		this.item = item;
	}

	/** @return the item to use, or {@code null} */
	public Item getItem() {
		return item;
	}
}
