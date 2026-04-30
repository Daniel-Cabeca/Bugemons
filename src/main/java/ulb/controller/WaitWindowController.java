package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

import ulb.view.windows.WaitWindow;
import ulb.view.WindowPath;

public class WaitWindowController implements WaitWindow.ViewListener {
	private final Stage stage;

	private WaitWindow view;
	private Stage WindowStage;

	public WaitWindowController(Stage stage) {
		this.stage = stage;
	}

	public void show() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.WAIT_WINDOW));
			loader.load();
			this.view = loader.getController();
			this.view.setViewListener(this);

			this.stage.getScene().setRoot(loader.getRoot());
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to load wait pop-up FXML: "+ e.getMessage());
		}
	}
}
