package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class NextRoomWindow {

	private ViewListener viewListener;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Handles the action to continue to the next room.
	 * @param event next event.
	 */
	@FXML
	public void handleContinue(ActionEvent event) {
		viewListener.onContinue();
	}

	/**
	 * Handles returning to the previous menu.
	 */
	@FXML
	public void handleReturn() {
		viewListener.onReturn();
	}

	public interface ViewListener {
		void onContinue();
		void onReturn();
	}
}
