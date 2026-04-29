package ulb.message.clientToServer;

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
    public void dispatch(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
