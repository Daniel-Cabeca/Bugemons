package ulb.view.windows;

import javafx.fxml.FXML;

public class WaitWindow {
	private ViewListener viewListener;

	public void setViewListener(ViewListener viewListener) { this.viewListener = viewListener; }

	@FXML
	private void initialize() {
	}

	public interface ViewListener {
	}
}
