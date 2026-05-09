package ulb.message.clientToServer;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class SendBattleRequestMessage implements ClientToServerMessage {
    private final String senderUsername;
    private final String receiverUsername;

    public SendBattleRequestMessage(String senderUsername, String receiverUsername) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.sendBattleRequest(senderUsername, receiverUsername);
    }
}

