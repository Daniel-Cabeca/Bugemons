package ulb.exceptions;

/**
 * Exception thrown when a requested domain entity cannot be found.
 *
 * This class intentionally extends NoSuchElementException for now because the
 * repository findById contracts already expose that exception type. It gives
 * the project a single, explicit exception to use for missing entities without
 * forcing a large repository API change in the same refactor step.
 */
public class EntityNotFoundException extends Exception {
	public EntityNotFoundException(String entityName, Object id) {
		super(entityName + " not found: " + id);
	}
}
