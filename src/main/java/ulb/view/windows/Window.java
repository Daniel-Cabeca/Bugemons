package ulb.view.windows;

import java.io.IOException;

import ulb.communication.Message;
import ulb.communication.types.SwitchWindowMessage;
import ulb.controller.GameController;

public abstract class Window{

	protected GameController gameController;

	public void setGameController(GameController gameController) { this.gameController = gameController; }

	public void onLoad() {}

	protected void sendWindowSwitchMessage(String fxmlPath) {
		gameController.handleMessage(new SwitchWindowMessage(fxmlPath));
	}
}
