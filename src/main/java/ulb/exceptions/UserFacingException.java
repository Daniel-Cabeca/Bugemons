package ulb.exceptions;

/**
 * Exception for blocking errors that can be shown directly to the player.
 */
public class UserFacingException extends GameException {
	public UserFacingException(String message) {
		super(message);
	}

	public UserFacingException(String message, Throwable cause) {
		super(message, cause);
	}
}
