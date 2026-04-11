package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ulb.communication.Message;
import ulb.communication.old_types.BattleEndInfoMessage;
import ulb.communication.old_types.GetInfoMessage;
import ulb.communication.old_types.InfoType;
import ulb.utils.Scaling;
import ulb.view.WindowPath;

public class BattleEndWindow extends Window {

	@FXML
	public Label titleLabel;
	@FXML
	public Label messageLabel;
	@FXML
	public Label gainXPLabel;
	@FXML
	private VBox content;

	private ViewListener listener;

	public void setListener(ViewListener listener) {
		this.listener = listener;
	}

	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	@Override
	public void onLoad() {
		Message response = sendMessage(new GetInfoMessage(InfoType.BATTLE_END));
		if (response instanceof BattleEndInfoMessage info) {
			setResult(info.isVictory(), info.getTotalXP());
		}
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
			gainXPLabel.setText("");
		}
	}

	/**
	 * Returns to the main menu
	 */
	public void handleReturn() {
		if (listener != null) {
			listener.onHandleReturn();
		} else {
			switchWindow(WindowPath.MODE);
		}
	}

	public interface ViewListener {
		void onHandleReturn();
	}
}