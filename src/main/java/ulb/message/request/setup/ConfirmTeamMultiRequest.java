package ulb.message.request.setup;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

import java.util.List;

public class ConfirmTeamMultiRequest implements Request {
	private final PlayerDTO opponent;
	private final List<BugemonDTO> team;

	public ConfirmTeamMultiRequest(PlayerDTO opponent, List<BugemonDTO> team) {
		this.opponent = opponent;
		this.team = team;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.setupMultiBattle(opponent, team);
	}
}
