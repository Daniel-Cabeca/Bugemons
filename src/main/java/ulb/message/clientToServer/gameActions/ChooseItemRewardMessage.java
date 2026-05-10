package ulb.message.clientToServer.gameActions;

import ulb.DTO.item.ItemDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseItemRewardMessage implements ClientToServerMessage{
	private final ItemDTO item;

	public ChooseItemRewardMessage(ItemDTO item){
		this.item = item;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseItemReward(item);
	}
}
