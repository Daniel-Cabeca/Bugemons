package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ulb.controller.GameController;
import javafx.scene.control.Label;

import java.io.IOException;

public class NextRoomWindow {
	@FXML
	public Label messageLabel;

	@FXML
	public Button nextButton;

	private GameController gameController;

	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}

	/**
	 * Goes to the next room in tower mode
	 *
	 * @param event the action triggered by clicking the continue button
	 * @throws IOException if the next room transition fails
	 */
	@FXML
	public void handleContinue(ActionEvent event) throws IOException {
		messageLabel.setText("Vous avez mis tous les Bugémons ennemis KO!");
		if (gameController != null) {
			gameController.handleTower(gameController.getTeam(), event);
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
