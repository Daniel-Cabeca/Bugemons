package ulb.communication.types;

import ulb.communication.Message;
import ulb.controller.GameController;

public class ConnectMessage implements Message{
    private String connectMessage;

    public ConnectMessage(String message) {
        this.connectMessage = message;
    }

    public String getConnectMessage() {
        return this.connectMessage;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CONNECT;
    }

    @Override
    public Message handle(GameController controller) {
        return controller.applyOn(this);
    }
}
