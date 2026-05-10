package ulb.message.request.setup;

import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;

public class SetUpTowerModeRequest implements Request {
	private final boolean newTower;

	public SetUpTowerModeRequest(boolean newTower){
		this.newTower = newTower;
	}

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.setupTowerMode(newTower);
	}
}
