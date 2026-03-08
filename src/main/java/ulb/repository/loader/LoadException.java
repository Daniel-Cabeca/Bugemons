package ulb.repository.loader;

/**
 * Exception thrown when failing to load the game data (parsing error, i/o error, …).
 */
public class LoadException extends RuntimeException {
	public LoadException(String message) {
		super(message);
	}
}
