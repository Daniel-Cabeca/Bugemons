package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;


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

    public MessageType getMessageType() {
        return MessageType.SWITCH_WINDOW;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
