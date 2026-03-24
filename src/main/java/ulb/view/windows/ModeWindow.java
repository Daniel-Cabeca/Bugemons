package ulb.view.windows;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ulb.controller.GameController;
import ulb.utils.Scaling;
import ulb.view.handler.Window;

public final class ModeWindow extends Window {

	private static final String NEXT_WINDOW = WINDOWS_PATH + "CreateTeamWindow.fxml";
	private GameController gameController;
	@FXML
	private VBox content;

	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	@FXML
	private void goSoloMode(ActionEvent event) {
		try {
			switchWindow(event, NEXT_WINDOW,gameController);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load FXML", e);
		}
	}

	@FXML
	private void quit() {
		Platform.exit();
		System.exit(0);
	}
	public void setGameController(GameController gameController) { this.gameController = gameController;}
}