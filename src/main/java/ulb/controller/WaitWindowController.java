package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import ulb.view.windows.WaitWindow;
import ulb.view.FxmlLoader;
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
		FXMLLoader loader = FxmlLoader.load(this, WindowPath.WAIT_WINDOW);
		this.view = loader.getController();
		this.view.setViewListener(this);

		this.getStage().getScene().setRoot(loader.getRoot());
	}
}
