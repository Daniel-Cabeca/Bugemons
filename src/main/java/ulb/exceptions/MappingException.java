package ulb.exceptions;

/**
 * Exception thrown when a DTO cannot be converted into a valid domain object.
 */
public class MappingException extends DataAccessException {
	public MappingException(String message) {
		super(message);
	}
}
