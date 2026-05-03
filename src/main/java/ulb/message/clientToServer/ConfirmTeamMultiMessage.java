package ulb.message.clientToServer;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
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
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

	public PlayerDTO getOpponent() { return this.opponent; }
	public List<BugemonDTO> getTeam() { return this.team; }
}
