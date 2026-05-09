package ulb.message.clientToServer.setup;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.server.ServerMessageHandler;

public class RegisterMessage implements ClientToServerMessage {
    private final PlayerRegisterDTO playerDTO;
    private final boolean login;

    public RegisterMessage(PlayerRegisterDTO playerDTO, boolean login){
        this.playerDTO = playerDTO;
        this.login = login;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.registerPlayer(playerDTO, login);
	}
}
