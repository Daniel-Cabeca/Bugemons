package ulb.message.clientToServer;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.DTO.player.PlayerDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class StartMultiBattleMessage implements ClientToServerMessage {
	PlayerDTO opponent;

	public StartMultiBattleMessage(PlayerDTO opponent) {
		this.opponent = opponent;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.startMultiBattle(opponent);
	}
}
