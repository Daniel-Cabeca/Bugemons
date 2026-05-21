package ulb.communication.Messenger;

import ulb.exceptions.CommunicationException;

import java.io.Serializable;

/**
 * Interface for in/out communication points between server and client.
 */
public interface Messenger {
	/**
	 * Send a message through the pipeline.
	 *
	 * @param message The message to send
	 * @throws CommunicationException If the message fails to be sent.
	 */
	void sendMessage(Serializable message) throws CommunicationException;

	/**
	 * Wait for and receive a message from the other end of the pipeline.
	 *
	 * @return The received message
	 * @throws CommunicationException If an error occurs in the pipeline
	 */
	Serializable receiveMessage() throws CommunicationException;
}
