package ulb.view.handler;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Node;
import ulb.communication.Message;
import ulb.controller.GameController;
import ulb.model.Player;
import ulb.utils.Scaling;
import ulb.view.windows.BattleEndWindow;
import ulb.view.windows.BattleWindow;
import ulb.view.windows.CreateTeamWindow;
import ulb.view.windows.ModeWindow;

public abstract class Window{
	protected static final String WINDOWS_PATH= "/ulb/view/";
	protected static final String MODE_WINDOW_PATH = WINDOWS_PATH + "ModeWindow.fxml";

	protected GameController gameController;

	protected Player player;

	public void setPlayer(Player player) { this.player = player; }


	public void switchWindow(ActionEvent event, String windowFxmlPath, GameController gameController) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource(windowFxmlPath));
		Parent root = loader.load();
		Object windowController = loader.getController();
		if (windowController instanceof CreateTeamWindow) {
			((CreateTeamWindow) windowController).setGameController(gameController);
		} else if (windowController instanceof ModeWindow) {
			((ModeWindow) windowController).setGameController(gameController);
		} else if (windowController instanceof BattleWindow) {
			((BattleWindow) windowController).setGameController(gameController);
		}
  		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}


	protected void sendMessage(Message m) throws IOException {
		gameController.handleMessage(m);
	}
}
