package ulb.message.clientToServer;

import ulb.DTO.player.PlayerDTO;

import ulb.Server.ServerMessageHandler;
import ulb.message.ClientToServerMessage;

public class RegisterMessage implements ClientToServerMessage {
    private PlayerDTO playerDTO;
    private boolean login;

    public RegisterMessage(PlayerDTO playerDTO, boolean login){
        this.playerDTO = playerDTO;
        this.login = login;
    }

    @Override
	public void dispatch(ServerMessageHandler handler) {
		handler.handle(this);
	}

    public PlayerDTO getPlayer() {return this.playerDTO;}
    public boolean isLogin() {return this.login;}
}
