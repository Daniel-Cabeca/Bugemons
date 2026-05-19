package ulb.message.request.gameActions;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class SwapBugemonRequest implements Request {
	private final BugemonDTO bugemonToSwap;

	public SwapBugemonRequest(BugemonDTO bugemonToSwap) {
		this.bugemonToSwap = bugemonToSwap;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseSwapBugemonAction(bugemonToSwap);
	}
}
