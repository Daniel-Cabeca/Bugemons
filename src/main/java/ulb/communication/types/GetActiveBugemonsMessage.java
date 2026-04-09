package ulb.communication.types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class GetActiveBugemonsMessage implements Message {
    @Override
    public MessageType getMessageType() {
        return MessageType.GET_ACTIVE_BUGEMONS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
