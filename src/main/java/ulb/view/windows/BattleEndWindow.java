package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;
import ulb.view.WindowPath;

import java.io.IOException;

public class BattleEndWindow extends Window{

	@FXML
	public Label titleLabel;
	@FXML
	public Label messageLabel;
	@FXML
	public Label gainXPLabel;
	@FXML
	private VBox content;

	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	/**
	 * Sets the result and message labels according to the battle result
	 *
	 * @param victory boolean indicating if the player's team won
	 */
	public void setResult(boolean victory, int totalXP) {
		if (victory) {
			titleLabel.setText("Victoire!");
			messageLabel.setText("Tous les Bugémons adverses ont été mis KO. Tu as gagné!");
			gainXPLabel.setText("XP gagné: " + totalXP);
		} else {
			titleLabel.setText("Défaite");
			messageLabel.setText("Tous tes Bugemons sont KO. Tu as perdu !");
		}
	}

	/**
	 * Returns to the main menu
	 */
	public void handleReturn() {
		sendWindowSwitchMessage(WindowPath.MODE);
	}
}