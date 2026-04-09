package ulb.communication.old_types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class ReceiveObjectRewardMessage implements Message{
    @Override
    public MessageType getMessageType(){ return MessageType.RECEIVE_OBJECT_REWARD; }

    @Override
    public Message handle(GameController controller){
        return controller.applyOn(this);
    }
}
