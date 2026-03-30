package ulb.communication.types;

import ulb.communication.Message;
import ulb.model.item.Item;

/**
 * Player chose to use an item in controlled battle
 */
public class UseItemRequestMessage implements Message {
	private final Item item;

	/**
	 * @param item the inventory item the player selected
	 */
	public UseItemRequestMessage(Item item) {
		this.item = item;
	}

	public Item getItem() {
		return item;
	}

	@Override
	public MessageType getMessageType() {
		return MessageType.USE_ITEM_REQUEST;
	}
}
