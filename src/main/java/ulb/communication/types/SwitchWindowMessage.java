package ulb.communication.types;

import ulb.communication.Message;


public class SwitchWindowMessage implements Message {
    private String switchWindow;

    public SwitchWindowMessage(String switchWindow) {
        this.switchWindow = switchWindow;
    }
    public String getSwitchWindow() {
        return this.switchWindow;
    }
}
