package ulb.message.request.gameInfo;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

import java.util.List;

public class GetAbilityEffectivenessRequest implements Request {
	private final List<AbilityDTO> abilities;
	private final BugemonDTO bugemonTarget;

	public GetAbilityEffectivenessRequest(List<AbilityDTO> abilities, BugemonDTO bugemonTarget) {
		this.abilities = abilities;
		this.bugemonTarget = bugemonTarget;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getAbilityEffectiveness(bugemonTarget, abilities);
	}
}
