package ulb.message.request.gameData;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetRandomAbilityRequest implements Request {
	private final BugemonDTO bugemon;

	public GetRandomAbilityRequest(BugemonDTO bugemon) {
		this.bugemon = bugemon;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getRandomAbility(bugemon);
	}
}
