package ulb.message.request.gameActions;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class ChooseAbilityRewardRequest implements Request {
	private final BugemonDTO bugemon;
	private final AbilityDTO oldAbility;
	private final AbilityDTO newAbility;

	public ChooseAbilityRewardRequest(BugemonDTO bugemon, AbilityDTO oldAbility, AbilityDTO newAbility) {
		this.bugemon = bugemon;
		this.oldAbility = oldAbility;
		this.newAbility = newAbility;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseAbilityReward(bugemon, oldAbility, newAbility);
	}
}
