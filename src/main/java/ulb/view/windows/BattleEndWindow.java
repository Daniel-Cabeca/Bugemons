package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ulb.controller.GameController;
import ulb.model.Player;
import ulb.view.handler.Window;

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

	private static final double BASE_WIDTH = 800;
	private static final double BASE_HEIGHT = 600;

	private Player player;


	public void setPlayer(Player player) {this.player = player;}

	@FXML
	public void initialize() {
		content.sceneProperty().addListener((obs, oldScene, scene) -> {
			if (scene != null) {

				scene.widthProperty().addListener((o, oldVal, newVal) -> scale(scene));
				scene.heightProperty().addListener((o, oldVal, newVal) -> scale(scene));

				scale(scene);
			}
		});
	}

	private void scale(javafx.scene.Scene scene) {
		double scaleX = scene.getWidth() / BASE_WIDTH;
		double scaleY = scene.getHeight() / BASE_HEIGHT;

		double scale = Math.min(scaleX, scaleY);

		content.setScaleX(scale);
		content.setScaleY(scale);
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
	 * @param event the action triggered by clicking the return button
	 * @throws IOException if the main menu FXML file cannot be loaded
	 */
	public void handleReturn(ActionEvent event) throws IOException {
		switchWindow(event, MODE_WINDOW_PATH,null);
	}
}