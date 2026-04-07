package ulb.communication.Messenger;

import ulb.communication.Message;

public interface Messenger {
    public void sendMessage(Message message) throws Exception;
    public Message receiveMessage() throws Exception;
}
