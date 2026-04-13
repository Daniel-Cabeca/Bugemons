package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetUserIdFromNameMessage implements ClientToServerMessage {
	private final String name;

	public GetUserIdFromNameMessage(String name) {
		this.name = name;
	}

	public String getName() { return this.name; }

	@Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}
}
