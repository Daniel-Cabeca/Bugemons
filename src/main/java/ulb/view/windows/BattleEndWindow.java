package ulb.view.windows;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;

/**
 * End screen displayed after winning/losing a battle.
 */
public class BattleEndWindow {

	@FXML
	public Label titleLabel;
	@FXML
	public Label messageLabel;
	@FXML
	public Label gainXPLabel;
	@FXML
	private VBox content;

	private ViewListener listener;

	public void setViewListener(ViewListener listener) {
		this.listener = listener;
	}

	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	/**
	 * Sets the result and message labels according to the battle result.
	 *
	 * @param victory boolean indicating if the player's team won
	 */
	public void setResult(boolean victory, int totalXP, Optional<String> opponent) {
		if (victory) {
			titleLabel.setText("Victoire!");
			if (opponent.isPresent()) {
				messageLabel.setText("Tous les Bugémons adverses ont été mis KO. Tu as gagné contre " + opponent.get() + " " + "!");
			} else {
				messageLabel.setText("Tous les Bugémons adverses ont été mis KO. Tu as gagné !");
			}
			gainXPLabel.setText("XP gagné: " + totalXP);
		} else {
			titleLabel.setText("Défaite");
			if (opponent.isPresent()) {
				messageLabel.setText("Tous tes Bugémons sont KO. Tu as perdu contre " + opponent.get() + " !");
			} else {
				messageLabel.setText("Tous tes Bugémons sont KO. Tu as perdu !");
			}
			gainXPLabel.setText("");
		}
	}

	/**
	 * Returns to the main menu.
	 */
	public void handleReturn() {
		listener.onHandleReturn();
	}

	/** 
	 * Handles the return button being pressed. 
	 */
	public interface ViewListener {
		void onHandleReturn();
	}
}