package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.communication.types.TowerNextRoomMessage;
import ulb.view.WindowPath;

import java.io.IOException;

public class NextRoomWindow extends Window {
	@FXML
	public Label messageLabel;

	@FXML
	public Button nextButton;

	// Sets the message depending on whether the player won or fled
	@Override
	public void onLoad() {
		if (viewManager.getGameController().hasFledBattle()) {
			messageLabel.setText("Vous avez fui le combat.");
			nextButton.setText("Réessayer");
			viewManager.getGameController().resetFledBattle();
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
		viewManager.handleMessage(new TowerNextRoomMessage(event));
	}

	@FXML
	public void handleReturn() {
		sendSwitchWindowMessage(WindowPath.MODE);
	}
}
