package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class NextRoomWindow extends Window {
	@FXML
	private Label messageLabel;
	@FXML
	private Button nextButton;

	private ViewListener viewListener;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Displays the end-of-battle message depending on whether the player fled.
	 * @param boolean saying if the player has quit or not.
	 */
	public void displayMessage(boolean hasFled) {
		if (hasFled) {
			messageLabel.setText("Vous avez fui le combat.");
			nextButton.setText("Réessayer");
		} else {
			messageLabel.setText("Vous avez mis tous les Bugémons ennemis KO!");
		}
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
