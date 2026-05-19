package ulb.message.response.gameInfo;

import ulb.message.response.Response;

public class TowerSavedInfoResponse extends Response {
	private final boolean isTowerSaved;

	public TowerSavedInfoResponse(boolean isTowerSaved) {
		this.isTowerSaved = isTowerSaved;
	}

	public boolean isTowerSaved() { return this.isTowerSaved; }
}
