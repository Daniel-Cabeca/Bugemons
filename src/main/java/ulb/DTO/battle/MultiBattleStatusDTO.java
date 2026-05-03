package ulb.DTO.battle;

import ulb.model.battle.MultiBattle;

import java.io.Serializable;

public class MultiBattleStatusDTO implements Serializable {
	public enum Status {
		NOT_CREATED,
		WAITING_ACCEPT,
		PICKING_TEAMS,
		BATTLE,
		END,
	}

	private Status status;

	public MultiBattleStatusDTO() {
		this.status = Status.NOT_CREATED;
	}

	public MultiBattleStatusDTO(MultiBattle battle) {
		if (battle.isAccepted()) {
			this.status = Status.PICKING_TEAMS;
		}
		else {
			this.status = Status.WAITING_ACCEPT;
		}
	}

	public Status getStatus() { return this.status; }
	public void setStatus(Status status) { this.status = status; }
}
