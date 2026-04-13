package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetChatMessagesMessage implements ClientToServerMessage {
    private final String usernameA;
    private final String usernameB;

    public GetChatMessagesMessage(String usernameA, String usernameB) {
        this.usernameA = usernameA;
        this.usernameB = usernameB;
    }

    public String getUsernameA() { return usernameA; }
    public String getUsernameB() { return usernameB; }

    @Override
    public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
