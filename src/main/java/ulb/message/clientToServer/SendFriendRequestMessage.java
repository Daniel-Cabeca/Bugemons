package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class SendFriendRequestMessage implements ClientToServerMessage {
    private final String senderUsername;
    private final String receiverUsername;

    public SendFriendRequestMessage(String senderUsername, String receiverUsername) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
    }

    public String getSenderUsername() { return senderUsername; }
    public String getReceiverUsername() { return receiverUsername; }

    @Override
    public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
