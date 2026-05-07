package ulb.exceptions;

import java.util.NoSuchElementException;

/**
 * Exception thrown when a requested domain entity cannot be found.
 *
 * This class intentionally extends NoSuchElementException for now because the
 * repository findById contracts already expose that exception type. It gives
 * the project a single, explicit exception to use for missing entities without
 * forcing a large repository API change in the same refactor step.
 */
public class EntityNotFoundException extends NoSuchElementException {
	public EntityNotFoundException(String entityName, Object id) {
		super(entityName + " not found: " + id);
	}

	public EntityNotFoundException(String message) {
		super(message);
	}
}
