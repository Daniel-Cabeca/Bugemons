package ulb.exceptions;

/**
 * Exception thrown when element already exists. 
 * For example trying to insert an ability in the database with an id that already exists.
 */
public class DuplicateElementException extends Exception {
	public DuplicateElementException(String message) {
		super(message);
	}
}
