package ulb.communication.types;

import javafx.event.ActionEvent;
import ulb.communication.Message;

/**
 * Message used to ask the controller to setup the next room in tower mode
 */
public class TowerNextRoomMessage implements Message {
    private ActionEvent event;

    public TowerNextRoomMessage(ActionEvent event) {
        this.event = event;
    }

    public ActionEvent getEvent() { return this.event; }

    @Override
    public MessageType getMessageType() {
        return MessageType.TOWER_NEXT_ROOM;
    }
}
