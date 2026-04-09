package ulb.communication.old_types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

/**
 * Message used by the view to request information/data needed for the display from the controller
 */
public class GetInfoMessage implements Message {
    private InfoType type;

    public GetInfoMessage(InfoType type) { this.type = type; }

    public InfoType getType() { return this.type; }

    @Override
    public MessageType getMessageType() {
        return MessageType.GET_INFO;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
