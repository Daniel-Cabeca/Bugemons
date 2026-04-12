package ulb.message.serverToClient;

import java.io.Serializable;

import ulb.DTO.item.ItemDTO;

public class RandomItemMessage implements Serializable{
	private ItemDTO item;

	public RandomItemMessage(ItemDTO item){
		this.item = item;
	}

	public ItemDTO getItem(){return this.item;}
}
