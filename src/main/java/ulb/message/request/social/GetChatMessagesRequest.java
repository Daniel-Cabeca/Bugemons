package ulb.message.request.social;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetChatMessagesRequest implements Request {
	private final String usernameA;
	private final String usernameB;

	public GetChatMessagesRequest(String usernameA, String usernameB) {
		this.usernameA = usernameA;
		this.usernameB = usernameB;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getChatMessages(usernameA, usernameB);
	}
}
