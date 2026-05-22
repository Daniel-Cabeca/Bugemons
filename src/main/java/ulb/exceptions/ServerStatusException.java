package ulb.exceptions;

/**
 * Exception occurs when the Client receives a unsuccessful StatusMessage from the Server 
 */
public class ServerStatusException extends Exception {
	public ServerStatusException(String message) {
		super(message);
	}
}
