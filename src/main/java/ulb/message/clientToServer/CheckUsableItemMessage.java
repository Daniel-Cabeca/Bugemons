package ulb.message.clientToServer;

import java.util.List;

import ulb.DTO.item.ItemDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class CheckUsableItemMessage implements ClientToServerMessage{
    private List<ItemDTO> items;

    public CheckUsableItemMessage(List<ItemDTO> items){
        this.items = items;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.checkUsableItems(items);
	}
}
