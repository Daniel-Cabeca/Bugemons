package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ulb.controller.GameController;
import javafx.scene.control.Label;

import java.io.IOException;

public class NextRoomWindow {
	@FXML
	public Label messageLabel;

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
}
