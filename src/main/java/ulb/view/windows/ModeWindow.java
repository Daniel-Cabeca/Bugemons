package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;

public class ModeWindow {

	@FXML
	private VBox content;

	private ViewListener viewListener;

	@FXML
	public void initialize() {
		Scaling.applyScaling(content);
	}

	@FXML
	private void goSoloMode() {
		viewListener.onSolo();
	}

	@FXML
	private void quit() {
		viewListener.quit();
	}

	public void setListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	public interface ViewListener {
		void onSolo();
		void quit();
	}
}