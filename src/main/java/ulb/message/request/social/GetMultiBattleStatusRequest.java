package ulb.message.request.social;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

/**
 * Fetches the status of the multiplayer battle session for two given players.
 */
public class GetMultiBattleStatusRequest implements Request {
	private final int userId1;
	private final int userId2;

	public GetMultiBattleStatusRequest(int userId1, int userId2) {
		this.userId1 = userId1;
		this.userId2 = userId2;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getMultiBattleStatus(userId1, userId2);
	}
}
