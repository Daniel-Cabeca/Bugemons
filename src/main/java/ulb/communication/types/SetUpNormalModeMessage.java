package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;

public class SetUpNormalModeMessage implements Message {
    @Override
    public MessageType getMessageType() {
        return MessageType.SETUP_NORMAL_MODE;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }
}
