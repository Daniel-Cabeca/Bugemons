package ulb.message.clientToServer;

import java.util.List;

import ulb.DTO.item.ItemDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class CheckUsableItemMessage implements ClientToServerMessage{
    private List<ItemDTO> items;

    public CheckUsableItemMessage(List<ItemDTO> items){
        this.items = items;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public List<ItemDTO> getItems(){return this.items;}
}
