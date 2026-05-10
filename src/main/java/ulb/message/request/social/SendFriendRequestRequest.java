package ulb.message.request.social;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;

public class SendFriendRequestRequest implements Request {
    private final String senderUsername;
    private final String receiverUsername;

    public SendFriendRequestRequest(String senderUsername, String receiverUsername) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.sendFriendRequest(senderUsername, receiverUsername);
    }
}
