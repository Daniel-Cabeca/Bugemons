package ulb.message.response.social;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.message.response.Response;

public class MultiBattleStatusResponse extends Response {
	private final MultiBattleStatusDTO status;

	public MultiBattleStatusResponse(MultiBattleStatusDTO status) {
		this.status = status;
	}

	public MultiBattleStatusDTO getStatus() { return this.status; }
}
