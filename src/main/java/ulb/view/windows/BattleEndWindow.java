package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ulb.model.Player;
import ulb.view.handler.Window;

import java.io.IOException;

public class BattleEndWindow extends Window{
	@FXML
	public Label titleLabel;
	@FXML
	public Label messageLabel;

	private Player player;

	public void setPlayer(Player player) {this.player = player;}

	/**
	 * Sets the result and message labels according to the battle result
	 *
	 * @param victory boolean indicating if the player's team won
	 */
	public void setResult(boolean victory) {
		if (victory) {
			titleLabel.setText("Victoire !");
			messageLabel.setText("Tous les Bugemons ennemy sont KO. Tu as gagné !");
		} else {
			titleLabel.setText("Défaite");
			messageLabel.setText("Tous tes Bugemons sont KO. Tu as perdu !");
		}
	}

	/**
	 * Returns to the main menu
	 * @param event the action triggered by clicking the return button
	 * @throws IOException if the main menu FXML file cannot be loaded
	 */
	public void handleReturn(ActionEvent event) throws IOException {
		switchWindow(event, MODE_WINDOW_PATH);

	}
}
