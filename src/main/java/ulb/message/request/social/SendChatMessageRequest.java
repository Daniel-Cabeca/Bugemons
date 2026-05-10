package ulb.message.request.social;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;

public class SendChatMessageRequest implements Request {
    private final String senderUsername;
    private final String receiverUsername;
    private final String content;

    public SendChatMessageRequest(String senderUsername, String receiverUsername, String content) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.sendChatMessage(senderUsername, receiverUsername, content);
    }
}
