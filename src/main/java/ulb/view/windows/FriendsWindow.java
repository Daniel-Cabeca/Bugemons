package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ulb.view.WindowPath;

public class FriendsWindow extends Window {

	@FXML
	VBox friendsList;

	@FXML
	private void goMultiplayerWindow() {
		sendSwitchWindowMessage(WindowPath.MULTIPLAYER);
	}
}
