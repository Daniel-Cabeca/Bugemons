package ulb.message.clientToServer;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;

import ulb.DTO.player.PlayerRegisterDTO;
import ulb.message.ClientToServerMessage;
import ulb.server.ServerMessageHandler;

public class RegisterMessage implements ClientToServerMessage {
    private PlayerRegisterDTO playerDTO;
    private boolean login;

    public RegisterMessage(PlayerRegisterDTO playerDTO, boolean login){
        this.playerDTO = playerDTO;
        this.login = login;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) throws UserFacingException, DataAccessException {
		handler.handle(this);
	}

    public PlayerRegisterDTO getPlayer() {return this.playerDTO;}
    public boolean isLogin() {return this.login;}
}
