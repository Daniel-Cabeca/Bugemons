package ulb.message.clientToServer.setup;

import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;

public class SetUpTowerModeMessage implements ClientToServerMessage {
	private final boolean newTower;

	public SetUpTowerModeMessage(boolean newTower){
		this.newTower = newTower;
	}

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.setupTowerMode(newTower);
	}
}
