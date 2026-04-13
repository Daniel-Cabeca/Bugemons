package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.MultiplayerWindow;

/**
 * Controller for multiplayer menu navigation.
 */
public class MultiplayerWindowController implements MultiplayerWindow.ViewListener {

	private Listener listener;
	private MultiplayerWindow view;
	private Stage stage;

	/**
	 * Creates the multiplayer controller.
	 *
	 * @param stage The application stage
	 * @param listener Listener handling multiplayer navigation
	 */
	public MultiplayerWindowController(Stage stage, Listener listener) {
		this.stage = stage;
		this.listener = listener;
	}


	/**
	 * Updates the listener instance.
	 *
	 * @param listener New listener
	 */
	public void setListener(Listener listener) {
		this.listener = listener;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGoFriendsWindow() {
		this.listener.onGoFriendsWindow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onGoModeWindow() {this.listener.goModeWindow();}


	/**
	 * Displays the multiplayer menu.
	 *
	 * @throws Exception If FXML loading fails
	 */
	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.MULTIPLAYER));
		loader.load();
		view = loader.getController();
		view.setListener(this);

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}


	/**
	 * Listener for multiplayer navigation actions.
	 */
	public interface Listener {
		/** Navigates to friends window. */
		void onGoFriendsWindow();
		/** Navigates back to mode window. */
		void goModeWindow();
		/**
		 * Registers the active multiplayer controller instance.
		 *
		 * @param controller Multiplayer controller instance
		 */
		void registerMultiplayerWindow(MultiplayerWindowController controller);
	}
}
