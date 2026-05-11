package ulb.controller.windows;

import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.controller.ClientController;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

/**
 * Controller for main mode selection actions.
 */
public class ModeController extends WindowController<ModeWindow> implements ModeWindow.ViewListener {
    /**
     * Creates the mode controller.
     *
     * @param stage The application stage
     * @param clientController The client controller
     */
    public ModeController(Stage stage, ClientController clientController) {
        super(stage, WindowPath.MODE, clientController);
        this.view.setViewListener(this);
    }

    /**
     * Notifies the listener to open the social menu or tab.
     */
    @Override
    public void onOpenSocial() {
        this.clientController.showWindow(WindowName.SOCIAL_PANEL);
    }


    /**
     * Notifies the listener to initiate the solo (single-player) game mode.
     */
    @Override
    public void onSolo() {
        this.clientController.showWindow(WindowName.GAME_MODE);
    }


    /**
     * Shuts down the JavaFX platform and terminates the application process.
     */
    @Override
    public void onQuit() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Notifies the listener to log the current user out of their session.
     */
    @Override
    public void onLogOut() {
        this.clientController.unsetPlayer();
        this.clientController.showWindow(WindowName.REGISTER);
    }
}
