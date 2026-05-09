package ulb.message.clientToServer;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseTowerRoomMessage implements ClientToServerMessage {
	private int roomId;

	public ChooseTowerRoomMessage(int roomId) {
		this.roomId = roomId;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseTowerRoom(roomId);
	}
	
}
