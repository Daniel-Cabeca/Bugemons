package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class AcceptBattleRequestMessage implements ClientToServerMessage {
    private final String receiverUsername;
    private final String senderUsername;

    public AcceptBattleRequestMessage(String receiverUsername, String senderUsername) {
        this.receiverUsername = receiverUsername;
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() { return receiverUsername; }
    public String getSenderUsername() { return senderUsername; }

    @Override
    public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
