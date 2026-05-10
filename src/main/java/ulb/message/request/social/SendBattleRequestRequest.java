package ulb.message.request.social;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class SendBattleRequestRequest implements Request {
    private final String senderUsername;
    private final String receiverUsername;

    public SendBattleRequestRequest(String senderUsername, String receiverUsername) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.sendBattleRequest(senderUsername, receiverUsername);
    }
}

