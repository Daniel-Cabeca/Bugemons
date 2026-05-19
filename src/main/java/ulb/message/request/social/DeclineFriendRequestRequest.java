package ulb.message.request.social;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class DeclineFriendRequestRequest implements Request {
	private final String receiverUsername;
	private final String senderUsername;

	public DeclineFriendRequestRequest(String receiverUsername, String senderUsername) {
		this.receiverUsername = receiverUsername;
		this.senderUsername = senderUsername;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.declineFriendRequest(senderUsername, receiverUsername);
	}
}
