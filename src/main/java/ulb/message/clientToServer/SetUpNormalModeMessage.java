package ulb.message.clientToServer;

import ulb.Server.ServerMessageHandler;
import ulb.message.ClientToServerMessage;

public class SetUpNormalModeMessage implements ClientToServerMessage {
    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}
}
