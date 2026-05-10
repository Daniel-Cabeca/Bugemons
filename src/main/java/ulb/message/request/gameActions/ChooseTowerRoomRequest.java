package ulb.message.request.gameActions;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class ChooseTowerRoomRequest implements Request {
	private final int roomId;

	public ChooseTowerRoomRequest(int roomId) {
		this.roomId = roomId;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseTowerRoom(roomId);
	}
	
}
