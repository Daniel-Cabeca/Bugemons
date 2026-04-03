package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import ulb.view.WindowPath;

public class FriendsWindow extends Window {
	// TODO: get list of friends after database is complete !

	@FXML
	VBox friendsList;

	@FXML
	TextField usernameField;

	@FXML
	private void goMultiplayerWindow() {
		sendSwitchWindowMessage(WindowPath.MULTIPLAYER);
	}

	@FXML
	private void inviteFriend() {
		System.out.println("inviter");
	}
}
