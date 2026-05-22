package ulb.exceptions;

/**
 * Exception thrown when the user inputs the wrong password.
 */
public class InvalidCredentialsException extends Exception {
	public InvalidCredentialsException() {
		super("Wrong password");
	}
}
