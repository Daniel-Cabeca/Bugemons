package ulb.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javafx.util.Duration;
import ulb.view.windows.WaitWindow;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;

public class WaitWindowController {
	private static final Duration CYCLE_DURATION = Duration.millis(500);

	private final ClientController clientController;
	private WaitWindow view;
	private Timeline timeline;

	public WaitWindowController(ClientController clientController, EventHandler waitCycle) {
		this.clientController = clientController;

		this.timeline = new Timeline(new KeyFrame(CYCLE_DURATION, waitCycle));
		this.timeline.setCycleCount(Timeline.INDEFINITE);
	}

	public Stage getStage() { return this.clientController.getStage(); }

	public void show() {
		FXMLLoader loader = FxmlLoader.load(this, WindowPath.WAIT_WINDOW);
		this.view = loader.getController();

		this.getStage().getScene().setRoot(loader.getRoot());
		this.timeline.play();
	}
}
