package ulb.repository.loader;

/**
 * Exception thrown when failing to load the game data (parsing error, i/o error, …).
 */
public class LoadFailureException extends RuntimeException {
	public LoadFailureException(String message) {
		super(message);
	}
}
