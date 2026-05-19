package ulb.communication.Messenger;

import java.io.Serializable;

import ulb.exceptions.CommunicationException;

public interface Messenger {
	void sendMessage(Serializable message) throws CommunicationException;
	Serializable receiveMessage() throws CommunicationException;
}
