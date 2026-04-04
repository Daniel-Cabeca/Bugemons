package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;

public class ErrorMessage implements Message {
    private String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return this.error;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.ERROR;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
