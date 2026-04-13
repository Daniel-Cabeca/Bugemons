package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.FriendsWindow;


/**
 * Controller for the friends management screen.
 */
public class FriendsController implements FriendsWindow.ViewListener{


	private FriendsWindow view;
	private Listener listener;
	private Stage stage;

	/**
	 * Creates the friends controller.
	 *
	 * @param stage The application stage
	 * @param listener Listener handling friends actions
	 */
	public FriendsController(Stage stage, Listener listener) {
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
	public void populateFriends(VBox friendsList) {
		this.listener.populateFriends(friendsList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void returnToMultiplayerWindow() {
		this.listener.returnToMultiplayerWindow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onInviteFriend(String usernameField){
		int id = this.listener.getUserIdFromName(usernameField);
		if (id > 0) {
			listener.addFriend(id);
			view.refreshFriendsList();
		}
	}

	/**
	 * Displays the friends screen.
	 *
	 * @throws Exception If FXML loading fails
	 */
	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.FRIENDS));
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
	 * Listener for friends screen actions.
	 */
	public interface Listener {
		/** Returns to the multiplayer window. */
		void returnToMultiplayerWindow();
		/**
		 * Adds a friend by user id.
		 *
		 * @param id The user id
		 */
		void addFriend(int id);
		/**
		 * Populates the visual friends list container.
		 *
		 * @param friendsList The friends list container
		 */
		void populateFriends(VBox friendsList);
		/**
		 * Resolves a user id from its username.
		 *
		 * @param name Username to resolve
		 * @return Matching user id, or invalid id when missing
		 */
		int getUserIdFromName(String name);
	}
}
