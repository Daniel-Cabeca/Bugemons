package ulb.message.request.gameActions;

import ulb.DTO.player.PlayerDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class QuitMultiBattleRequest implements Request {
	private final PlayerDTO opponent;

	public QuitMultiBattleRequest(PlayerDTO opponent) {
		this.opponent = opponent;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.quitMultiBattle(opponent);
	}
}
