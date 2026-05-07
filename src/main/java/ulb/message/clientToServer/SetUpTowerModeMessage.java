package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

public class SetUpTowerModeMessage implements ClientToServerMessage {
	private boolean newTower;

	public SetUpTowerModeMessage(boolean newTower){
		this.newTower = newTower;
	}

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.handle(this);
	}

	public boolean isNewTower(){ return this.newTower; }
}  
