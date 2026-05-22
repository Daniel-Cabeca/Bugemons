package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.model.GameMode;
import ulb.utils.Scaling;

import java.util.List;

/**
 * View for the team confirmation screen.
 * Displays the player's team and the selected game mode before starting a battle.
 */
public class ConfirmTeamWindow {
	@FXML
	private GridPane playerTeamGrid;
	@FXML
	private VBox content;
	@FXML
	private Label gameModeLabel;
	private ViewListener viewListener;

	/**
	 * Applies scaling on initialization.
	 */
	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	/**
	 * Sets the listener to be notified of team confirmation events.
	 *
	 * @param viewListener The view listener to register
	 */
	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Sets the game mode label according the selected game mode.
	 *
	 * @param gameMode The selected game mode
	 */
	public void setGameModeLabel(GameMode gameMode) {
		String message = "";
		switch (gameMode) {
			case AUTO:
				message = "Mode de jeu : automatique";
				break;
			case CONTROLLED:
				message = "Mode de jeu : classique";
				break;
			case TOWER:
				message = "Mode de jeu : Tour NO";
				break;
			default:
				message = "Mode de jeu inconnu";
		}
		gameModeLabel.setText(message);
	}

	/**
	 * Handles the return button click.
	 */
	@FXML
	private void handleReturn() {
		viewListener.onReturn();
	}

	/**
	 *
	 * Handles the confirm button click.
	 */
	@FXML
	private void handleConfirm() { viewListener.onConfirm(); }

	/**
	 * Displays the chosen team.
	 *
	 * @param team The team to display
	 */
	public void displayTeam(List<BugemonDTO> team) {
		playerTeamGrid.getChildren().clear();
		int row = 0;
		int col = 0;
		int count = 0;
		// for each Bugemon it displays its sprite, name and stats
		for (BugemonDTO bugemon : team) {
			VBox cell = new VBox();
			Image image = new Image(bugemon.getSpritePath());
			ImageView sprite = new ImageView(image);
			sprite.setFitWidth(50);
			sprite.setFitHeight(50);
			sprite.setPreserveRatio(true);
			Label name = new Label(bugemon.getName() + " (Niveau: " + bugemon.level() + ")");
			name.setStyle("-fx-font-weight: bold;");
			Label stats =
					new Label("PV: " + bugemon.getHp() + " ATK: " + bugemon.getAttack() + " DEF: " + bugemon.getDefense() + " INIT: " + bugemon.getInitiative());

			cell.getChildren().addAll(name, sprite, stats);
			playerTeamGrid.add(cell, col, row++);
			count++;
			if (count == 3) { // put 3 bugemons in each col
				col++;
				row = 0;
			}
		}
	}

	/**
	 * Listener for team confirmation view events.
	 */
	public interface ViewListener {
		/** 
		 * Handles the player confirming their team. 
		 */
		void onConfirm();

		/** 
		 * Handles the return button being pressed. 
		 */
		void onReturn();
	}
}