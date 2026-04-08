package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;

public class SuccessMessage implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.SUCCESS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
