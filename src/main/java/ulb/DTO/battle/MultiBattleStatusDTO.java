package ulb.DTO.battle;

import java.io.Serializable;

public class MultiBattleStatusDTO implements Serializable {
	public enum Status {
		NOT_CREATED,
		WAITING_ACCEPT,
		PICKING_TEAMS,
		BATTLE,
		END,
		DECLINED,
	}

	private Status status;

	public MultiBattleStatusDTO() {
		this.status = Status.NOT_CREATED;
	}

	public Status getStatus() { return this.status; }
	public void setStatus(Status status) { this.status = status; }
}
