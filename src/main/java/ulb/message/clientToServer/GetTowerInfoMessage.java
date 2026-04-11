package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetTowerInfoMessage implements ClientToServerMessage {
    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}
}
