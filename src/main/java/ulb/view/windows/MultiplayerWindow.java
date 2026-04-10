package ulb.view.windows;

import javafx.fxml.FXML;
import ulb.view.WindowPath;

public class MultiplayerWindow extends Window{

	private ViewListener listener;

	public void setListener(ViewListener listener) {
		this.listener = listener;
	}

	@FXML
	private void goFriendsWindow() {
		this.listener.onGoFriendsWindow();
	}

	@FXML
	private void goModeWindow () {
		this.listener.onGoModeWindow();
	}

	public interface ViewListener {
		void onGoFriendsWindow();
		void onGoModeWindow();
	}
}
