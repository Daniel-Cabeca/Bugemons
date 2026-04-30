package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

import ulb.view.windows.WaitWindow;
import ulb.view.WindowPath;

public class WaitWindowController implements WaitWindow.ViewListener {
	private final ClientController clientController;

	private WaitWindow view;
	private Stage WindowStage;

	public WaitWindowController(ClientController clientController) {
		this.clientController = clientController;
	}

	public Stage getStage() { return this.clientController.getStage(); }

	public void show() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.WAIT_WINDOW));
			loader.load();
			this.view = loader.getController();
			this.view.setViewListener(this);

			this.getStage().getScene().setRoot(loader.getRoot());
		}
		catch (IOException e) {
			throw new RuntimeException("Failed to load wait pop-up FXML: "+ e.getMessage());
		}
	}
}
