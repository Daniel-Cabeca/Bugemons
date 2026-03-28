package ulb.view.windows;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ulb.communication.Message;
import ulb.communication.types.SwitchWindowMessage;
import ulb.utils.Scaling;
import ulb.view.ViewManager;
import ulb.view.WindowPath;

public final class ModeWindow extends Window {

	@FXML
	private VBox content;

	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	@FXML
	private void goSoloMode() {
		sendSwitchWindowMessage(WindowPath.CREATE_TEAM);
	}

	@FXML
	private void quit() {
		Platform.exit();
		System.exit(0);
	}
}