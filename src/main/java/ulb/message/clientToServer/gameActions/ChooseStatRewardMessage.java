package ulb.message.clientToServer.gameActions;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseStatRewardMessage implements ClientToServerMessage{
	private final BugemonDTO bugemon;

	public ChooseStatRewardMessage(BugemonDTO bugemon){
		this.bugemon = bugemon;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseStatReward(bugemon);
	}
}
