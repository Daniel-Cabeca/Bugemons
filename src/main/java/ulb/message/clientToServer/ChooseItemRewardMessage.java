package ulb.message.clientToServer;

import ulb.DTO.item.ItemDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseItemRewardMessage implements ClientToServerMessage{
	private ItemDTO item;

	public ChooseItemRewardMessage(ItemDTO item){
		this.item = item;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.handle(this);
	}

	public ItemDTO getItem(){return this.item;}
}
