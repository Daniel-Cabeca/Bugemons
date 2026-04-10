package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;
import ulb.view.WindowPath;

public class FriendsWindow extends Window {
	// TODO: get list of friends after database is complete !
	private ViewListener listener;

	public void setListener(ViewListener listener) {
		this.listener = listener;
	}

	@FXML
	public void initialize() {
		this.listener.populateFriends(this.friendsList);
	}

	@FXML
	VBox friendsList;

	@FXML
	TextField usernameField;

	@FXML
	private void  returnToMultiplayerWindow() {
		this.listener.returnToMultiplayerWindow();
	}

	@FXML
	private void inviteFriend() {
		this.listener.onInviteFriend(this.usernameField.toString());
	}

	public interface ViewListener {
		void returnToMultiplayerWindow();
		void onInviteFriend(String usernameField);
		void populateFriends(VBox friendsList);
	}
}
