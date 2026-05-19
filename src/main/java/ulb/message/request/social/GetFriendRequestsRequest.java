package ulb.message.request.social;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetFriendRequestsRequest implements Request {
	private final String username;

	public GetFriendRequestsRequest(String username) { this.username = username; }

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getFriendRequests(username);
	}
}
