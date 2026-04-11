package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetActiveBugemonsMessage implements ClientToServerMessage {
    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}
}
