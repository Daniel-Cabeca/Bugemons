package ulb.message.response.gameData;

import ulb.DTO.item.ItemDTO;
import ulb.message.response.Response;

public class RandomItemResponse extends Response {
	private final ItemDTO item;

	public RandomItemResponse(ItemDTO item) {
		this.item = item;
	}

	public ItemDTO getItem() { return this.item; }
}
