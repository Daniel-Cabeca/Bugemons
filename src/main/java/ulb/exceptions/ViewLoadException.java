package ulb.exceptions;

/**
 * Exception thrown when a JavaFX view or FXML file cannot be loaded.
 */
public class ViewLoadException extends Exception {
	public ViewLoadException(String message) {
		super(message);
	}

	public ViewLoadException(String message, Throwable cause) {
		super(message, cause);
	}
}
