package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;

/**
 * View for the register and login screen.
 */
public class RegisterWindow {
	@FXML
	private VBox content;
	@FXML
	private TextField usernameField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Label errorLabel;
	private ViewListener viewListener;

	/**
	 * Sets the listener to be notified of register and login events.
	 *
	 * @param viewListener The view listener to register
	 */
	public void setViewListener(ViewListener viewListener) { this.viewListener = viewListener; }

	/**
	 * Applies scaling on initialization.
	 */
	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	/**
	 * Handles the sign-up action.
	 */
	@FXML
	private void onSignUp() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText();
		if (username.isEmpty() || password.isEmpty()) {
			setErrorLabel("Remplis tous les champs.");
			return;
		}
		viewListener.onSignUp(username, password);
	}

	/**
	 * Displays an error message in the UI.
	 *
	 * @param error The error message to display
	 */
	public void setErrorLabel(String error) {
		errorLabel.setText(error);
	}

	/**
	 * Handles the login action.
	 */
	@FXML
	private void onLogin() {
		String username = usernameField.getText().trim();
		String password = passwordField.getText();
		if (username.isEmpty() || password.isEmpty()) {
			setErrorLabel("Remplis tous les champs.");
			return;
		}
		this.viewListener.onLogin(username, password);
	}

	/**
	 * Listener for register and login view events.
	 */
	public interface ViewListener {
		/** 
		 * Handles the login attempt with the given username and password. 
		*/
		void onLogin(String username, String password);
		/** 
		 * Handles the sign-up attempt with the given username and password.
		*/
		void onSignUp(String username, String password);
	}
}