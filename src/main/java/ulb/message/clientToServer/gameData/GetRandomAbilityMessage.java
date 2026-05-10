package ulb.message.clientToServer.gameData;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.server.ServerMessageHandler;

public class GetRandomAbilityMessage implements ClientToServerMessage{
	private final BugemonDTO bugemon;

	public GetRandomAbilityMessage(BugemonDTO bugemon){
		this.bugemon = bugemon;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getRandomAbility(bugemon);
	}
}
