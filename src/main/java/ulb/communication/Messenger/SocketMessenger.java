package ulb.communication.Messenger;

import ulb.exceptions.CommunicationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the Messenger interface using sockets.
 */
public class SocketMessenger implements Messenger {
	/**
	 * Object used for logging runtime information to the console or to a log file.
	 */
	private static final Logger LOGGER = Logger.getLogger(SocketMessenger.class.getName());

	/**
	 * The socket connection.
	 */
	private final Socket socket;

	/**
	 * Input stream for the socket.
	 */
	private final ObjectInputStream reader;

	/**
	 * Output stream for the socket.
	 */
	private final ObjectOutputStream writer;

	public SocketMessenger(Socket socket) throws CommunicationException {
		if (socket == null) {
			throw new IllegalArgumentException("Socket cannot be null.");
		}

		this.socket = socket;
		try {
			this.writer = new ObjectOutputStream(this.socket.getOutputStream());
			this.writer.flush();
			this.reader = new ObjectInputStream(this.socket.getInputStream());

		} catch (IOException e) {
			this.close();
			throw new CommunicationException("Impossible to open network's communication.");
		}
	}

	/**
	 * Close the socket.
	 */
	public void close() {
		if (!socket.isClosed()) {
			try {
				this.reader.close();
				this.writer.close();
				this.socket.close();
			} catch (IOException e) {
				LOGGER.log(Level.WARNING, "Error while closing socket connection.");
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendMessage(Serializable message) throws CommunicationException {
		if (message == null) {
			throw new IllegalArgumentException("Message cannot be null.");
		}

		try {
			this.writer.writeObject(message);
			this.writer.reset();
			this.writer.flush();
		} catch (IOException e) {
			this.close();
			throw new CommunicationException("Impossible to send network message.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Serializable receiveMessage() throws CommunicationException {
		try {
			return (Serializable) reader.readObject();
		} catch (IOException e) {
			this.close();
			throw new CommunicationException("Impossible to receive network message.");
		} catch (ClassNotFoundException e) {
			this.close();
			throw new CommunicationException("Unrecognized network message.");
		}
	}
}
