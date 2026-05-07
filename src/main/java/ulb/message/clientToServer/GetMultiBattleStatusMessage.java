package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

/**
 * Fetches the status of the multiplayer battle session for two given players.
 */
public class GetMultiBattleStatusMessage implements ClientToServerMessage {
	private int userId1;
	private int userId2;

	public GetMultiBattleStatusMessage(int userId1, int userId2) {
		this.userId1 = userId1;
		this.userId2 = userId2;
	}

	public int getUserId1() { return this.userId1; }
	public int getUserId2() { return this.userId2; }

	@Override
	public void dispatch(ServerMessageHandler handler) { handler.handle(this); }
}
