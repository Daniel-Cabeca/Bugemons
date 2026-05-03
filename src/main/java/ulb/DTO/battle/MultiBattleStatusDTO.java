package ulb.DTO.battle;

import ulb.model.battle.MultiBattle;

import java.io.Serializable;

public class MultiBattleStatusDTO implements Serializable {
	private boolean created = true;
	private boolean accepted = false;

	public MultiBattleStatusDTO() {
		this.created = false;
	}

	public MultiBattleStatusDTO(MultiBattle battle) {
		this.accepted = battle.isAccepted();
	}

	public boolean isCreated() { return this.created; }
	public void setCreated(boolean created) { this.created = created; }

	public boolean isAccepted() { return this.accepted; }
	public void setAccepted(boolean accepted) { this.accepted = accepted; }
}
