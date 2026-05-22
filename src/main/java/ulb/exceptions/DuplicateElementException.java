package ulb.exceptions;

/**
 * Exception thrown when a duplicate element is inserted where uniqueness is required.
 */
public class DuplicateElementException extends Exception {
	public DuplicateElementException(String message) {
		super(message);
	}
}