package ulb.exceptions;

/**
 * Exception thrown when the user is trying to register while already having an account.
 */
public class UserAlreadyExistsException extends Exception {
	public UserAlreadyExistsException(String username) {
		super(username + " already exists in database");
	}
}
