package ulb.message.request.gameActions;

import ulb.DTO.ability.AbilityDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class UseAbilityRequest implements Request {
	private final AbilityDTO ability;

	public UseAbilityRequest(AbilityDTO ability) {
		this.ability = ability;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseUseAbilityAction(ability);
	}
}
