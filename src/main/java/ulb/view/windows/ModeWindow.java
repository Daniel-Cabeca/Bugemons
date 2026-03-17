package ulb.view.windows;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ulb.view.handler.Window;

public final class ModeWindow extends Window {

	private static final String NEXT_WINDOW = WINDOWS_PATH + "CreateTeamWindow.fxml";

	@FXML
	private VBox content;

	private static final double BASE_WIDTH = 800;
	private static final double BASE_HEIGHT = 600;

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

	@FXML
	private void goSoloMode(ActionEvent event) {
		try {
			switchWindow(event, NEXT_WINDOW);
		} catch (IOException e) {
			throw new RuntimeException("Cannot load FXML", e);
		}
	}

	@FXML
	private void quit() {
		Platform.exit();
	}
}