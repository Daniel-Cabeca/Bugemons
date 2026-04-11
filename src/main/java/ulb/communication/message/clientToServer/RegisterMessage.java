package ulb.communication.message.clientToServer;

import ulb.DTO.player.PlayerDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class RegisterMessage implements Message {
    private PlayerDTO playerDTO;
    private boolean login;

    public RegisterMessage(PlayerDTO playerDTO, boolean login){
        this.playerDTO = playerDTO;
        this.login = login;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.REGISTER;
    };

    @Override
    public Message handle(GameController controller) {
        return null;
    };

    public PlayerDTO getPlayer() {return this.playerDTO;}
    public boolean isLogin() {return this.login;}
}
