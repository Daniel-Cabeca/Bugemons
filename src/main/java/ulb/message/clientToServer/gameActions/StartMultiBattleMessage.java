package ulb.message.clientToServer.gameActions;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.DTO.player.PlayerDTO;
import ulb.server.ServerMessageHandler;

public class StartMultiBattleMessage implements ClientToServerMessage {
	private final PlayerDTO opponent;

	public StartMultiBattleMessage(PlayerDTO opponent) {
		this.opponent = opponent;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.startMultiBattle(opponent);
	}
}
