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
    private Listener listener;
    /**
     * Creates the mode controller.
     *
     * @param stage The application stage
     * @param listener Listener handling mode actions
     */
    public ModeController(Stage stage, Listener listener) throws ViewLoadException {
        super(stage,WindowPath.MODE);
        this.view.setViewListener(this);
        this.listener = listener;
    }
    /**
     * Notifies the listener to open the social menu or tab.
     */
    @Override
    public void onOpenSocial() {
        listener.onShowSocialPanel();
    }


    /**
     * Notifies the listener to initiate the solo (single-player) game mode.
     */
    @Override
    public void onSolo() {
        listener.onShowGameModeWindow();
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
        listener.onLogOut();
    }

    /**
     * Listener for mode screen actions.
     */
    public interface Listener {
        /** Opens the social panel. */
        void onShowSocialPanel();
        /** Starts solo flow. */
        void onShowGameModeWindow();
        /** Logs out the current user. */
        void onLogOut();
    }
}
