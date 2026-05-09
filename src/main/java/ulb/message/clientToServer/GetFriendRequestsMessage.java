package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetFriendRequestsMessage implements ClientToServerMessage {
    private final String username;

    public GetFriendRequestsMessage(String username) { this.username = username; }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.getFriendRequests(username);
    }
}
