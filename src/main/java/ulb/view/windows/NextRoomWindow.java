package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for asking the player to continue after a battle in the tower.
 */
public class NextRoomWindow {
	/**
	 * Listener for the view's inputs.
	 */
	private ViewListener viewListener;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Handles the action to continue to the next room.
	 *
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

	/**
	 * Listener for the view's inputs.
	 */
	public interface ViewListener {
		/**
		 * Handles continue action.
		 */
		void onContinue();

		/**
		 * Handles return action.
		 */
		void onReturn();
	}
}
