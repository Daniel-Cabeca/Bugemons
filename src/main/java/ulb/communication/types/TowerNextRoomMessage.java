package ulb.communication.types;

import javafx.event.ActionEvent;
import ulb.communication.Message;

public class TowerNextRoomMessage implements Message {
    private ActionEvent event;

    public TowerNextRoomMessage(ActionEvent event) {
        this.event = event;
    }

    public ActionEvent getEvent() { return this.event; }
}
