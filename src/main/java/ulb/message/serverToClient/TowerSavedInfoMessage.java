package ulb.message.serverToClient;

import java.io.Serializable;

public class TowerSavedInfoMessage implements Serializable {
	boolean isTowerSaved;

	public TowerSavedInfoMessage(boolean isTowerSaved){
		this.isTowerSaved = isTowerSaved;
	}

	public boolean isTowerSaved(){ return this.isTowerSaved; }
}
