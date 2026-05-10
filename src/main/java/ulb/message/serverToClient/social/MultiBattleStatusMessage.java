package ulb.message.serverToClient.social;

import ulb.DTO.battle.MultiBattleStatusDTO;

import java.io.Serializable;

public class MultiBattleStatusMessage implements Serializable {
	private final MultiBattleStatusDTO status;

	public MultiBattleStatusMessage(MultiBattleStatusDTO status) {
		this.status = status;
	}

	public MultiBattleStatusDTO getStatus() { return this.status; }
}
