package ulb.communication.types;

import ulb.communication.Message;


/**
 * Message used by the view to indicate that the window should be switched
 */
public class SwitchWindowMessage implements Message {
    private String switchWindow;

    public SwitchWindowMessage(String switchWindow) {
        this.switchWindow = switchWindow;
    }
    public String getSwitchWindow() {
        return this.switchWindow;
    }
}
