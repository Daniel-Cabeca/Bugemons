package ulb.message.response.playerInfo;

import ulb.DTO.item.ItemDTO;
import ulb.message.response.Response;

import java.util.Map;

public class PlayerInventoryResponse extends Response {
	private final Map<ItemDTO, Integer> inventoryDTO;

	public PlayerInventoryResponse(Map<ItemDTO, Integer> inventoryDTO) {
		this.inventoryDTO = inventoryDTO;
	}

	public Map<ItemDTO, Integer> getInventory() {
		return this.inventoryDTO;
	}

}
