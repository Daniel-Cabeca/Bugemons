package ulb.message.clientToServer.gameInfo;

import java.util.List;

import ulb.DTO.item.ItemDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class CheckUsableItemMessage implements ClientToServerMessage{
    private final List<ItemDTO> items;

    public CheckUsableItemMessage(List<ItemDTO> items){
        this.items = items;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.checkUsableItems(items);
	}
}
