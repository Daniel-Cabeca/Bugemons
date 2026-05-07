package ulb.exceptions;

/**
 * Base exception for blocking errors related to the game domain.
 *
 * Use this exception, or one of its subclasses, when an expected game
 * situation prevents the requested action from continuing.
 */
public class GameException extends Exception {
	public GameException(String message) {
		super(message);
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}
}
