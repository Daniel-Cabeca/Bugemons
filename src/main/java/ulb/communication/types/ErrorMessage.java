package ulb.communication.types;

import ulb.communication.Message;

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
}
