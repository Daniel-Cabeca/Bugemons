package ulb.mapper.battle;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.battle.MultiBattleStatusDTO.Status;
import ulb.model.battle.MultiBattleSession;

/**
 * Used to create a status DTO for a multiplayer battle session.
 */
public class MultiBattleStatusMapper {

	private MultiBattleStatusMapper() {}

	public static MultiBattleStatusDTO toDTO(MultiBattleSession battle) {
		MultiBattleStatusDTO.Status status = Status.NOT_CREATED;

		if (battle.isDeclined()) {
			status = Status.DECLINED;
		} else if (battle.isReady()) {
			status = Status.BATTLE;
		} else if (battle.isAccepted()) {
			status = Status.PICKING_TEAMS;
		} else {
			status = Status.WAITING_ACCEPT;
		}

		return new MultiBattleStatusDTO(status);
	}

}
