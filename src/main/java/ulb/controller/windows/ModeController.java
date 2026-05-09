package ulb.controller.windows;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.exceptions.ViewLoadException;
import ulb.view.FxmlLoader;
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
     * @param clientListener listener to communicate with the clientController
     */
    public ModeController(Stage stage, ClientListener clientListener) {
        super(stage, WindowPath.MODE, clientListener);
        this.view.setViewListener(this);
    }

    /**
     * Notifies the listener to open the social menu or tab.
     */
    @Override
    public void onOpenSocial() {
        this.clientListener.onShowWindow(WindowName.SOCIAL_PANEL);
    }


    /**
     * Notifies the listener to initiate the solo (single-player) game mode.
     */
    @Override
    public void onSolo() {
        this.clientListener.onShowWindow(WindowName.GAME_MODE);
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
        this.clientListener.onLogOut();
    }
}
