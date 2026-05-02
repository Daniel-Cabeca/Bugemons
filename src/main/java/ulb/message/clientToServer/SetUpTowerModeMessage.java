package ulb.message.clientToServer;

import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class SetUpTowerModeMessage implements ClientToServerMessage {
	private boolean newTower;

	public SetUpTowerModeMessage(boolean newTower){
		this.newTower = newTower;
	}

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

	public boolean isNewTower(){ return this.newTower; }
}  
