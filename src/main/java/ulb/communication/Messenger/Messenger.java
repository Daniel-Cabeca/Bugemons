package ulb.communication.Messenger;

import ulb.exceptions.CommunicationException;

import java.io.Serializable;

public interface Messenger {
	void sendMessage(Serializable message) throws CommunicationException;
	Serializable receiveMessage() throws CommunicationException;
}
