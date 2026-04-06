package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;

public class ReceiveObjectRewardMessage implements Message{
    @Override
    public MessageType getMessageType(){ return MessageType.RECEIVE_OBJECT_REWARD; }

    @Override
    public Message handle(GameController controller){
        return controller.applyOn(this);
    }
}
