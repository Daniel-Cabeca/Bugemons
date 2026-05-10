package ulb.message.clientToServer.gameActions;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseTowerRoomMessage implements ClientToServerMessage {
	private final int roomId;

	public ChooseTowerRoomMessage(int roomId) {
		this.roomId = roomId;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseTowerRoom(roomId);
	}
	
}
