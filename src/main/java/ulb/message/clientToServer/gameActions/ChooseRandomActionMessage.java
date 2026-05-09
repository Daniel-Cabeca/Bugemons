package ulb.message.clientToServer.gameActions;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseRandomActionMessage implements ClientToServerMessage {
	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseRandomAction();
	}
}
