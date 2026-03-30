package ulb.communication.types;

import ulb.communication.Message;

// Sent when the player flees a tower battle
public class TowerFleeMessage implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.TOWER_FLEE;
    }
}
