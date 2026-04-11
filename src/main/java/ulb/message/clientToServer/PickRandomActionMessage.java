package ulb.message.clientToServer;

import ulb.Server.ServerMessageHandler;
import ulb.message.ClientToServerMessage;

public class PickRandomActionMessage implements ClientToServerMessage {
    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}
}
