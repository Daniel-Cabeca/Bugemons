package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class SendChatMessageMessage implements ClientToServerMessage {
    private final String senderUsername;
    private final String receiverUsername;
    private final String content;

    public SendChatMessageMessage(String senderUsername, String receiverUsername, String content) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.content = content;
    }

    public String getSenderUsername() { return senderUsername; }
    public String getReceiverUsername() { return receiverUsername; }
    public String getContent() { return content; }

    @Override
    public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
