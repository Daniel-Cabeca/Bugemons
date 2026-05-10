package ulb.message.request.gameActions;

import ulb.DTO.item.ItemDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class ChooseItemRewardRequest implements Request {
	private final ItemDTO item;

	public ChooseItemRewardRequest(ItemDTO item){
		this.item = item;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseItemReward(item);
	}
}
