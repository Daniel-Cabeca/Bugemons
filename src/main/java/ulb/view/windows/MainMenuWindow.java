package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;

/**
 * Class for the Mode selection window.
 * Handles UI interactions and scaling for the main menu options.
 */
public class MainMenuWindow {

	@FXML
	private VBox content;

	private ViewListener viewListener;

	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	/**
	 * Notifies the action to open the social panel.
	 */
	@FXML
	private void openSocialPanel() {
		viewListener.onOpenSocial();
	}

	/**
	 * Notifies the action to start the solo game mode.
	 */
	@FXML
	private void goSoloMode() {
		viewListener.onSolo();
	}

	/**
	 * Notifies the action to quit the application.
	 */
	@FXML
	private void quit() {
		viewListener.onQuit();
	}

	/**
	 * Notifies the action to log the user out of the current session.
	 */
	@FXML
	private void logOut() { viewListener.onLogOut(); }

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	public interface ViewListener {
		/**
		 * Notifies the listener to open the social menu or tab.
		 */
		void onOpenSocial();

		/**
		 * Notifies the listener to initiate the solo (single-player) game mode.
		 */
		void onSolo();

		/**
		 * Shuts down the JavaFX platform and terminates the application process.
		 */
		void onQuit();

		/**
		 * Notifies the listener to log the current user out of their session.
		 */
		void onLogOut();
	}
}
