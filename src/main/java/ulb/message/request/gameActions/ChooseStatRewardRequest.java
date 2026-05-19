package ulb.message.request.gameActions;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class ChooseStatRewardRequest implements Request {
	private final BugemonDTO bugemon;

	public ChooseStatRewardRequest(BugemonDTO bugemon) {
		this.bugemon = bugemon;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseStatReward(bugemon);
	}
}
