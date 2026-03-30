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

	/**
	 * Goes to the next room in tower mode
	 *
	 * @param event the action triggered by clicking the continue button
	 * @throws IOException if the next room transition fails
	 */
	@FXML
	public void handleContinue(ActionEvent event) throws IOException {
		messageLabel.setText("Vous avez mis tous les Bugémons ennemis KO!");
		viewManager.handleMessage(new TowerNextRoomMessage(event));
	}

	@FXML
	public void handleReturn(ActionEvent event) {
		sendSwitchWindowMessage(WindowPath.MODE);
	}
}
