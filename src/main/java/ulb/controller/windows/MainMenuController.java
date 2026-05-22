package ulb.controller.windows;

import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.view.WindowPath;
import ulb.view.windows.MainMenuWindow;

/**
 * Controller for main mode selection actions.
 */
public class MainMenuController extends WindowController<MainMenuWindow> implements MainMenuWindow.ViewListener {
	/**
	 * Creates the mode controller.
	 *
	 * @param stage The application stage
	 * @param clientController The client controller
	 */
	public MainMenuController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.MAIN_MENU, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOpenSocial() {
		this.clientController.showWindow(WindowName.SOCIAL_PANEL);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSolo() {
		this.clientController.showWindow(WindowName.GAME_MODE);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onQuit() {
		Platform.exit();
		System.exit(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLogOut() {
		this.clientController.unsetPlayer();
		this.clientController.showWindow(WindowName.REGISTER);
	}
}
