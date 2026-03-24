package ulb.communication.types;

import javafx.event.ActionEvent;
import ulb.communication.Message;


public class SwitchWindowMessage implements Message {
    private String switchWindow;
    private ActionEvent event;

    public SwitchWindowMessage(String switchWindow, ActionEvent event) {
        this.switchWindow = switchWindow;
        this.event = event;
    }
    public String getSwitchWindow() {
        return this.switchWindow;
    }

    public ActionEvent getEvent() { return this.event; }
}
