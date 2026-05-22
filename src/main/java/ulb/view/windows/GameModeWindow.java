package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;

/**
 * View for the game mode selection.
 */
public class GameModeWindow {
	@FXML
	private VBox content;
	@FXML
	private Button continueTowerButton;
	private ViewListener viewListener;

	/**
	 * Applies scaling on initialization.
	 */
	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	/**
	 * Sets the listener to be notified of game mode selection events.
	 *
	 * @param viewListener The view listener to be set
	 */
	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/**
	 * Handles the automatic battle button click.
	 */
	@FXML
	public void handleAutomaticBattle() {
		viewListener.onAutoBattle();
	}

	/**
	 * Handles the controlled battle button click.
	 */
	@FXML
	public void handleControlledBattle() {
		viewListener.onControlledBattle();
	}

	/**
	 * Handles the new tower battle button click.
	 */
	@FXML
	public void handleNewTowerBattle() {
		viewListener.onTowerMode(true);
	}

	/**
	 * Handles the continue tower battle button click.
	 */
	@FXML
	public void handleContinueTowerBattle() {
		viewListener.onTowerMode(false);
	}

	/**
	 * Handles the return button click.
	 */
	@FXML
	private void handleReturn() {
		viewListener.onReturn();
	}

	/**
	 * Enables or disables the continue tower button.
	 *
	 * @param activateButton {@code true} to enable the button, {@code false} to disable it
	 */
	public void activateContinueTowerButton(boolean activateButton) {
		continueTowerButton.setDisable(!activateButton);
	}

	/**
	 * Listener for game mode selection view events.
	 */
	public interface ViewListener {
		/** 
		 * Handles the automatic battle mode being selected. 
		 */
		void onAutoBattle();

		/** 
		 * Handles the controlled battle mode being selected. 
		 */
		void onControlledBattle();

		/** 
		 * Handles the tower mode being selected. 
		 */
		void onTowerMode(boolean newTower);
		
		/** 
		 * Handles the return button being pressed. 
		 */
		void onReturn();
	}
}