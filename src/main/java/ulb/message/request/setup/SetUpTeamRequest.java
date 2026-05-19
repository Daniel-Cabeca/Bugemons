package ulb.message.request.setup;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

import java.util.List;

public class SetUpTeamRequest implements Request {
	private final List<BugemonDTO> team;

	public SetUpTeamRequest(List<BugemonDTO> team) {
		this.team = team;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.setupTeam(team);
	}
}
