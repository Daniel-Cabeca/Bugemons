package ulb.message.request.social;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class DeclineBattleRequestRequest implements Request {
	private final String receiverUsername;
	private final String senderUsername;

	public DeclineBattleRequestRequest(String receiverUsername, String senderUsername) {
		this.receiverUsername = receiverUsername;
		this.senderUsername = senderUsername;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.declineBattleRequest(senderUsername, receiverUsername);
	}
}

