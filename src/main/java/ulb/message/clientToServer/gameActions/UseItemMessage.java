package ulb.message.clientToServer.gameActions;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.DTO.item.ItemDTO;
import ulb.server.ServerMessageHandler;

public class UseItemMessage implements ClientToServerMessage{
    private final ItemDTO item;

    public UseItemMessage(ItemDTO item){
        this.item = item;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseUseItemAction(item);
	}
}
