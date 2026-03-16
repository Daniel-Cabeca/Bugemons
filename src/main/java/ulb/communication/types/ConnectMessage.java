package ulb.communication.types;

import ulb.communication.Message;

public class ConnectMessage implements Message{
    private String connectMessage;

    public ConnectMessage(String message) {
        this.connectMessage = message;
    }

    public String getConnectMessage() {
        return this.connectMessage;
    }
}
