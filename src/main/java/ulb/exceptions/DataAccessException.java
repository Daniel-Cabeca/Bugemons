package ulb.exceptions;

/**
 * Exception thrown when a technical error occurs while accessing persisted data.
 */
public class DataAccessException extends Exception {
	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}
}
