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
		MultiBattleStatusDTO dto = new MultiBattleStatusDTO();

		if (battle.isReady()) {
			dto.setStatus(Status.BATTLE);
		}
		else if (battle.isAccepted()) {
			dto.setStatus(Status.PICKING_TEAMS);
		}
		else {
			dto.setStatus(Status.WAITING_ACCEPT);
		}

		return dto;
	}

}
