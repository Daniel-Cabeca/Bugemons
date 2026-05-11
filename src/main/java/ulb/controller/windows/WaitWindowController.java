package ulb.controller.windows;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.stage.Stage;

import javafx.util.Duration;
import ulb.controller.ClientController;
import ulb.view.windows.WaitWindow;
import ulb.view.WindowPath;

public class WaitWindowController extends WindowController<WaitWindow> {
	private static final Duration CYCLE_DURATION = Duration.millis(500);
	private Timeline timeline;

	public WaitWindowController(Stage stage, ClientController clientController){
		super(stage, WindowPath.WAIT_WINDOW, clientController);
	}

	public void setNewTimeLine(EventHandler waitCycle){
		this.timeline = new Timeline(new KeyFrame(CYCLE_DURATION, waitCycle));
		this.timeline.setCycleCount(Timeline.INDEFINITE);
	}

	public void show(){
		super.show();
		this.timeline.play();
	}

	public void stop() {
		this.timeline.stop();
	}
}
