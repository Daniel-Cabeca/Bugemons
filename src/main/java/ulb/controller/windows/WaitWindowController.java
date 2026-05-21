package ulb.controller.windows;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import ulb.controller.ClientController;
import ulb.view.WindowPath;
import ulb.view.windows.WaitWindow;

/**
 * Controller for making the player wait.
 * Runs a callback in a loop. The callback is responsible for switching to a different controller when waiting is over.
 */
public class WaitWindowController extends WindowController<WaitWindow> {
	/**
	 * The duration to wait between each call to the callback.
	 */
	private static final Duration CYCLE_DURATION = Duration.millis(500);

	/**
	 * FXML Timeline running the callback in a loop.
	 */
	private Timeline timeline;

	public WaitWindowController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.WAIT_WINDOW, clientController);
	}

	/**
	 * Change the FXML Timeline.
	 *
	 * @param waitCycle The callback to run in the Timeline.
	 */
	public void setNewTimeLine(EventHandler waitCycle) {
		this.timeline = new Timeline(new KeyFrame(CYCLE_DURATION, waitCycle));
		this.timeline.setCycleCount(Timeline.INDEFINITE);
	}

	/**
	 * Display the waiting window and start the callback loop.
	 */
	@Override
	public void show() {
		super.show();
		this.timeline.play();
	}

	/**
	 * Stop the callback loop.
	 */
	public void stop() {
		this.timeline.stop();
	}
}
