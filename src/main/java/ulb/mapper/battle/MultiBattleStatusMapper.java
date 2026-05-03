package ulb.mapper.battle;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.battle.MultiBattleStatusDTO.Status;
import ulb.model.battle.MultiBattle;

/**
 * Used to create a status DTO for a multiplayer battle session.
 */
public class MultiBattleStatusMapper {

	private MultiBattleStatusMapper() {}

	public static MultiBattleStatusDTO toDTO(MultiBattle battle) {
		MultiBattleStatusDTO dto = new MultiBattleStatusDTO();

		if (battle.isAccepted()) {
			dto.setStatus(Status.PICKING_TEAMS);
		}
		else {
			dto.setStatus(Status.WAITING_ACCEPT);
		}

		return dto;
	}

}
