package ulb.communication.Messenger;

import java.io.Serializable;

public interface Messenger {
    public void sendMessage(Serializable message) throws Exception;
    public Serializable receiveMessage() throws Exception;
}
