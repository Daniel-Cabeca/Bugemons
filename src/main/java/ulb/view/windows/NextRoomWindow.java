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

	public void displayMessage(boolean hasFled) {
		if (hasFled) {
			messageLabel.setText("Vous avez fui le combat.");
			nextButton.setText("Réessayer");
		} else {
			messageLabel.setText("Vous avez mis tous les Bugémons ennemis KO!");
		}
	}

	/**
	 * Goes to the next room in tower mode
	 *
	 * @param event the action triggered by clicking the continue button
	 */
	@FXML
	public void handleContinue(ActionEvent event) {
		viewListener.onContinue();
	}

	@FXML
	public void handleReturn() {
		viewListener.onReturn();
	}

	public interface ViewListener {
		void onContinue();
		void onReturn();
	}
}
