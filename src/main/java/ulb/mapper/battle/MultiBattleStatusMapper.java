package ulb.mapper.battle;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.model.battle.MultiBattle;

/**
 * Used to create a status DTO for a multiplayer battle session.
 */
public class MultiBattleStatusMapper {

	private MultiBattleStatusMapper() {}

	public static MultiBattleStatusDTO toDTO(MultiBattle battle) {
		MultiBattleStatusDTO status = new MultiBattleStatusDTO();

		status.setAccepted(battle.isAccepted());

		return status;
	}

}
