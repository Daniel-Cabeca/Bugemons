package ulb.view.windows;

import javafx.fxml.FXML;
import ulb.view.WindowPath;

public class MultiplayerWindow extends Window{

	@FXML
	private void goFriendsWindow() {
		sendSwitchWindowMessage(WindowPath.FRIENDS);
	}

	@FXML // TODO: for iteration 4
	private void goLeaderboardWindow() {

	}

	@FXML
	private void goModeWindow () {
		sendSwitchWindowMessage(WindowPath.MODE);
	}
}
