package ulb.communication.message.clientToServer;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class RunMessage implements Message{
    @Override
    public MessageType getMessageType() {
        return MessageType.RUN;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
