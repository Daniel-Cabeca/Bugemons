package ulb.message.clientToServer;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

import java.util.List;

public class ConfirmTeamMultiMessage implements ClientToServerMessage {
	private PlayerDTO opponent;
	private List<BugemonDTO> team;

	public ConfirmTeamMultiMessage(PlayerDTO opponent, List<BugemonDTO> team) {
		this.opponent = opponent;
		this.team = team;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.setupMultiBattle(opponent, team);
	}
}
