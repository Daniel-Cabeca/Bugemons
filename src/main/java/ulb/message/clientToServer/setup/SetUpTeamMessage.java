package ulb.message.clientToServer.setup;

import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;

public class SetUpTeamMessage implements ClientToServerMessage {
    private final List<BugemonDTO> team;

    public SetUpTeamMessage(List<BugemonDTO> team){
        this.team = team;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.setupTeam(team);
	}
}
