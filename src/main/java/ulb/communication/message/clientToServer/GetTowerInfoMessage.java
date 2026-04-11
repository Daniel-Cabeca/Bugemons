package ulb.communication.message.clientToServer;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class GetTowerInfoMessage implements Message {
    @Override
    public MessageType getMessageType() {
        return MessageType.GET_TOWER_INFO;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
