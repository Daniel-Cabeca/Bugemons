package ulb.exceptions;

/**
 * Exception thrown when attempting to create a user that already exists in the database.
 */
public class UserAlreadyExistsException extends Exception {
	public UserAlreadyExistsException(String username) {
		super(username + " already exists in database");
	}
}