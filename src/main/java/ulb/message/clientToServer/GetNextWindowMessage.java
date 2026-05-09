package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetNextWindowMessage implements ClientToServerMessage {
	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getNextWindow();
	}
}
