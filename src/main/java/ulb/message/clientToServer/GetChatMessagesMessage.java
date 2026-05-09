package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetChatMessagesMessage implements ClientToServerMessage {
    private final String usernameA;
    private final String usernameB;

    public GetChatMessagesMessage(String usernameA, String usernameB) {
        this.usernameA = usernameA;
        this.usernameB = usernameB;
    }

    @Override
    public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
        handler.getChatMessages(usernameA, usernameB);
    }
}
