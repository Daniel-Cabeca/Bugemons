package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetFriendRequestsMessage implements ClientToServerMessage {
    private final String username;

    public GetFriendRequestsMessage(String username) { this.username = username; }

    public String getUsername() { return username; }

    @Override
    public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
