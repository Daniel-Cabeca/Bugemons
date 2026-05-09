package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class DeclineFriendRequestMessage implements ClientToServerMessage {
    private final String receiverUsername;
    private final String senderUsername;

    public DeclineFriendRequestMessage(String receiverUsername, String senderUsername) {
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.declineFriendRequest(senderUsername, receiverUsername );
    }
}
