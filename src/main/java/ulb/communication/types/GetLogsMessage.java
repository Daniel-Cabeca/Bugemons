package ulb.communication.types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class GetLogsMessage implements Message {
    @Override
    public MessageType getMessageType() {
        return MessageType.GET_LOGS;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
