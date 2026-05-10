package ulb.message.request.social;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class AcceptFriendRequestRequest implements Request {
    private final String receiverUsername;
    private final String senderUsername;

    public AcceptFriendRequestRequest(String receiverUsername, String senderUsername) {
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.acceptFriendRequest(senderUsername, receiverUsername );
    }

}

