package ulb.message.clientToServer;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

public class RunMessage implements ClientToServerMessage{
   	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseRunAction();
	}
}
