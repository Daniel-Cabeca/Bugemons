package ulb.message.clientToServer;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class ChooseStatRewardMessage implements ClientToServerMessage{
	private BugemonDTO bugemon;

	public ChooseStatRewardMessage(BugemonDTO bugemon){
		this.bugemon = bugemon;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.chooseStatReward(bugemon);
	}
}
