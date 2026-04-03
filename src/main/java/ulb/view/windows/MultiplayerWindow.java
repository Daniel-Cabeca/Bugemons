package ulb.view.windows;

import javafx.fxml.FXML;
import ulb.view.WindowPath;

public class MultiplayerWindow extends Window{

	@FXML
	private void goFriendsWindow() {
		sendSwitchWindowMessage(WindowPath.FRIENDS);
	}

	@FXML
	private void goLeaderboardWindow() {

	}

	@FXML
	private void goModeWindow () {
		sendSwitchWindowMessage(WindowPath.MODE);
	}
}
