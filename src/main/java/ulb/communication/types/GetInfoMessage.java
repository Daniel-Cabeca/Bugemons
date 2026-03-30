package ulb.communication.types;

import ulb.communication.Message;

/**
 * Message used by the view to request information/data needed for the display from the controller
 */
public class GetInfoMessage implements Message {
    private InfoType type;

    public GetInfoMessage(InfoType type) { this.type = type; }

    public InfoType getType() { return this.type; }
}
