package ulb.communication.Messenger;

import java.io.Serializable;

public interface Messenger {
    void sendMessage(Serializable message);
    Serializable receiveMessage();
}
