package ulb.message.serverToClient.gameData;

import java.io.Serializable;

import ulb.DTO.item.ItemDTO;

public class RandomItemMessage implements Serializable{
	private final ItemDTO item;

	public RandomItemMessage(ItemDTO item){
		this.item = item;
	}

	public ItemDTO getItem(){return this.item;}
}
