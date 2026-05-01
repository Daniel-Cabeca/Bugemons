package ulb.exceptions;

/**
 * Exception thrown when a communication error occurs between client and server.
 */
public class CommunicationException extends RuntimeException {
	public CommunicationException(String message) {
		super(message);
	}

	public CommunicationException(String message, Throwable cause) {
		super(message, cause);
	}
}
