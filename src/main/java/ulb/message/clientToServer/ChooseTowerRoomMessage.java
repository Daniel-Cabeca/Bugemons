package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseTowerRoomMessage implements ClientToServerMessage {
	private int roomId;

	public ChooseTowerRoomMessage(int roomId) {
		this.roomId = roomId;
	}

	public int getRoomId() {return roomId;}

	@Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}
	
}
