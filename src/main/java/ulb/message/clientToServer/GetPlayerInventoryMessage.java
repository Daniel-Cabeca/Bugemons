package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetPlayerInventoryMessage implements ClientToServerMessage {
    private String userName;

    public GetPlayerInventoryMessage(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.handle(this);
    }
}
