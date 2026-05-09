package ulb.message.clientToServer.playerInfo;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetPlayerInventoryMessage implements ClientToServerMessage {
    private final String username;

    public GetPlayerInventoryMessage(String username) {
        this.username = username;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.getPlayerInventory(username);
    }
}
