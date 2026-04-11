package ulb.message.clientToServer;

import ulb.DTO.item.ItemDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class UseItemMessage implements ClientToServerMessage{
    private ItemDTO item;

    public UseItemMessage(ItemDTO item){
        this.item = item;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public ItemDTO getItem(){return this.item;}
}
