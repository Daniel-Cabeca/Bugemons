package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.controller.GameController;
import ulb.view.ViewManager;

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
		GameController controller = viewManager.getGameController();
		if (controller != null) {
			controller.handleTower(controller.getTeam(), event);
		}
	}

	/**
	 * Updates the text on the button to indicate if it is new room or floor
	 *
	 * @param isFloorCompleted true if it is a new floor, false if it is a new room (not floor)
	 */
	public void updateButtonText(boolean isFloorCompleted) {
		if (isFloorCompleted) {
			nextButton.setText("Prochain étage");
		} else {
			nextButton.setText("Prochaine salle");
		}
	}
}
