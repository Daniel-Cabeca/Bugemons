package ulb.message.clientToServer.social;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class DeclineBattleRequestMessage implements ClientToServerMessage {
    private final String receiverUsername;
    private final String senderUsername;

    public DeclineBattleRequestMessage(String receiverUsername, String senderUsername) {
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.declineBattleRequest(senderUsername, receiverUsername);
    }
}

