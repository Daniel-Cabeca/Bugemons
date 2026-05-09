package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
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

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getMultiBattleStatus(userId1, userId2);
	}
}
