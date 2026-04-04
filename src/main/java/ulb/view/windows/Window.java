package ulb.view.windows;

import ulb.communication.Message;
import ulb.controller.GameController;

public abstract class Window {

	protected GameController gameController;

	public void setGameController(GameController gameController) {
		this.gameController = gameController;
	}

	public void onLoad() {}

	protected Message sendMessage(Message message) {
		return gameController.handleMessage(message);
	}

	protected void switchWindow(String fxmlPath) {
		gameController.switchWindow(fxmlPath);
	}
}
