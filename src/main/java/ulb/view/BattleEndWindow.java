package ulb.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ulb.model.Player;

import java.io.IOException;

public class BattleEndWindow {

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
			titleLabel.setText("Victory!");
			messageLabel.setText("All enemy Bugemons are KO. You win!");
		} else {
			titleLabel.setText("Defeat");
			messageLabel.setText("All your Bugemons are KO. You lost!");
		}
	}

	/**
	 * Returns to the main menu
	 * @param event the action triggered by clicking the return button
	 * @throws IOException if the main menu FXML file cannot be loaded
	 */
	public void handleReturn(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/MainMenu.fxml"));
		Parent root = loader.load();
		MainMenu controller = loader.getController();
		controller.setPlayer(player);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);

	}
}
