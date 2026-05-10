package ulb.message.clientToServer.gameActions;

import ulb.DTO.player.PlayerDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class QuitMultiBattleMessage implements ClientToServerMessage  {
	private final PlayerDTO opponent;

	public QuitMultiBattleMessage(PlayerDTO opponent) {
		this.opponent = opponent;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.quitMultiBattle(opponent);
	}
}
