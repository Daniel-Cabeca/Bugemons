package ulb.message.clientToServer.gameData;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetRandomItemMessage implements ClientToServerMessage{
	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getRandomItem();
	}
}
