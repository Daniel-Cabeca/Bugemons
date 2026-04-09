package ulb.communication.old_types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

// Sent when the player flees a tower battle
public class TowerFleeMessage implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.TOWER_FLEE;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
