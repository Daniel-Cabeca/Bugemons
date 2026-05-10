package ulb.message.request.setup;

import java.util.List;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.server.ServerMessageHandler;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;

public class SetUpTeamRequest implements Request {
    private final List<BugemonDTO> team;

    public SetUpTeamRequest(List<BugemonDTO> team){
        this.team = team;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.setupTeam(team);
	}
}
