package ulb.communication.message.clientToServer;

import ulb.DTO.player.PlayerDTO;
import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class SetUpPlayerMessage implements Message {
    private PlayerDTO player;

    public SetUpPlayerMessage(PlayerDTO player){
        this.player = player;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.SETUP_PLAYER;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public PlayerDTO getPlayer(){return this.player;}
}
