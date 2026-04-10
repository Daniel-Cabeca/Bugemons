package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.MultiplayerWindow;

public class MultiplayerWindowController implements MultiplayerWindow.ViewListener {

	private Listener listener;
	private MultiplayerWindow view;
	private Stage stage;

	public MultiplayerWindowController(Stage stage, Listener listener) {
		this.stage = stage;
		this.listener = listener;
	}


	public void setListener(Listener listener) {
		this.listener = listener;

	}

	@Override
	public void onGoFriendsWindow() {
		this.listener.onGoFriendsWindow();
	}

	@Override
	public void onGoModeWindow() {this.listener.goModeWindow();}


	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.MULTIPLAYER));
		loader.load();
		view = loader.getController();
		view.setListener(this);

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}


	public interface Listener {
		void onGoFriendsWindow();
		void goModeWindow();
	}
}
