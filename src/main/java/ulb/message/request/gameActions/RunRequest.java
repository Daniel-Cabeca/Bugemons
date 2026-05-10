package ulb.message.request.gameActions;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;

public class RunRequest implements Request {
   	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseRunAction();
	}
}
