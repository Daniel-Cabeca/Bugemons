package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;

/**
 * Sent from the battle view when the player requests one automatic turn (AUTO mode).
 */
public class AutoTurnRequestMessage implements Message {

    @Override
    public MessageType getMessageType() {
        return MessageType.AUTO_TURN_REQUEST;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
