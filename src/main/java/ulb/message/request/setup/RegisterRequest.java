package ulb.message.request.setup;

import ulb.DTO.player.PlayerRegisterDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.request.Request;
import ulb.server.ServerMessageHandler;

public class RegisterRequest implements Request {
	private final PlayerRegisterDTO playerDTO;
	private final boolean login;

	public RegisterRequest(PlayerRegisterDTO playerDTO, boolean login) {
		this.playerDTO = playerDTO;
		this.login = login;
	}

	@Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.registerPlayer(playerDTO, login);
	}
}
