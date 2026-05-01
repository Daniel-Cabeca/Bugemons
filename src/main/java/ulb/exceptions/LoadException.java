package ulb.exceptions;

/**
 * Exception thrown when failing to load the game data (parsing error, i/o error, …).
 */
public class LoadException extends DataAccessException {
	public LoadException(String message) {
		super(message);
	}

	public LoadException(String message, Throwable cause) {
		super(message, cause);
	}
}
