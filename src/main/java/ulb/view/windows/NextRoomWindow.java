package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.communication.types.TowerNextRoomMessage;
import ulb.view.WindowPath;

public class NextRoomWindow extends Window {
	@FXML
	public Label messageLabel;

	@FXML
	public Button nextButton;

	// Sets the message depending on whether the player won or fled
	@Override
	public void onLoad() {
		if (gameController.hasFledBattle()) {
			messageLabel.setText("Vous avez fui le combat.");
			nextButton.setText("Réessayer");
			gameController.resetFledBattle();
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
		sendMessage(new TowerNextRoomMessage(event));
	}

	@FXML
	public void handleReturn() {
		switchWindow(WindowPath.MODE);
	}
}
