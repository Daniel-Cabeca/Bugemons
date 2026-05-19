package ulb.exceptions;

public class UserAlreadyExistsException extends Exception {
	public UserAlreadyExistsException(String username) {
		super(username + " already exists in database");
	}
}
