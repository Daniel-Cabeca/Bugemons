package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;

public class TowerSavedInfoMessage implements Serializable {
	private final boolean isTowerSaved;

	public TowerSavedInfoMessage(boolean isTowerSaved){
		this.isTowerSaved = isTowerSaved;
	}

	public boolean isTowerSaved(){ return this.isTowerSaved; }
}
