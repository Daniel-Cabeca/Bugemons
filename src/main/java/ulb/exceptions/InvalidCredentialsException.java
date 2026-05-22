package ulb.exceptions;

/**
 * Exception thrown when invalid credentials are provided during authentication.
 */
public class InvalidCredentialsException extends Exception {
	public InvalidCredentialsException() {
		super("Wrong password");
	}
}