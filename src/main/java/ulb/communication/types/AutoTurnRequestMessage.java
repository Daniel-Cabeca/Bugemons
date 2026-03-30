package ulb.communication.types;

import ulb.communication.Message;

/**
 * Sent from the battle view when the player requests one automatic turn (AUTO mode).
 */
public class AutoTurnRequestMessage implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.AUTO_TURN_REQUEST;
    }
}
