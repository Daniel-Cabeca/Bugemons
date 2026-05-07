package ulb.message.clientToServer;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class GetRandomAbilityMessage implements ClientToServerMessage{
	private BugemonDTO bugemon;

	public GetRandomAbilityMessage(BugemonDTO bugemon){
		this.bugemon = bugemon;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.handle(this);
	}

	public BugemonDTO getBugemon(){return this.bugemon;}
}
