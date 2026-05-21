package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.AbandonTowerRequest;
import ulb.view.WindowPath;
import ulb.view.windows.NextRoomWindow;

/**
 * Controller for the transition screen between tower rooms.
 */
public class NextRoomController extends WindowController<NextRoomWindow> implements NextRoomWindow.ViewListener {
	/**
	 * Creates the next room controller.
	 *
	 * @param stage The application stage
	 * @param clientController The controller used for navigation
	 */
	public NextRoomController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.NEXT_ROOM, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onContinue() { this.clientController.nextRoom(); }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn() {
		if (this.clientController.postData(new AbandonTowerRequest())) {
			this.clientController.showWindow(WindowName.MAIN_MENU);
		}
	}
}
