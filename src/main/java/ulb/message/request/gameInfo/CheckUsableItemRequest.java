package ulb.message.request.gameInfo;

import ulb.DTO.item.ItemDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

import java.util.List;

public class CheckUsableItemRequest implements Request {
	private final List<ItemDTO> items;

	public CheckUsableItemRequest(List<ItemDTO> items) {
		this.items = items;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.checkUsableItems(items);
	}
}
