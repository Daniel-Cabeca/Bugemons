package ulb.message.request.playerInfo;

import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class GetPlayerTeamRequest implements Request {
	private String username;

	public GetPlayerTeamRequest(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.getPlayerTeam(this.username);
	}
}
