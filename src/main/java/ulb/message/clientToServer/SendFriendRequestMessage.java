package ulb.message.clientToServer;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

public class SendFriendRequestMessage implements ClientToServerMessage {
    private final String senderUsername;
    private final String receiverUsername;

    public SendFriendRequestMessage(String senderUsername, String receiverUsername) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.sendFriendRequest(senderUsername, receiverUsername);
    }
}
