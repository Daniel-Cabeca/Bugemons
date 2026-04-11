package ulb.communication.message.clientToServer;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class PickRandomActionMessage implements Message {
    @Override
    public MessageType getMessageType() {
        return MessageType.PICK_RANDOM_ACTION;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
