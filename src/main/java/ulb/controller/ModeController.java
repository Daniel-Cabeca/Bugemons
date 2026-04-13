package ulb.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

/**
 * Controller for main mode selection actions.
 */
public class ModeController implements ModeWindow.ViewListener {

    private Listener listener;
    private Stage stage;
    private ModeWindow view;

    /**
     * Creates the mode controller.
     *
     * @param stage The application stage
     * @param listener Listener handling mode actions
     */
    public ModeController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the mode selection screen.
     *
     * @throws Exception If FXML loading fails
     */
    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.MODE));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    /**
     * Notifies the listener to open the social menu or tab.
     */
    @Override
    public void onOpenSocial() {
        listener.onOpenSocial();
    }

    /**
     * Notifies the listener to initiate the solo (single-player) game mode.
     */
    @Override
    public void onSolo() {
        listener.onSolo();
    }

    /**
     * Notifies the listener to initiate the multiplayer game mode.
     */
    @Override
    public void onMultiplayer() {
        listener.onMultiplayer();
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
        void onOpenSocial();
        /** Starts solo flow. */
        void onSolo();
        /** Starts multiplayer flow. */
        void onMultiplayer();
        /** Logs out the current user. */
        void onLogOut();
    }
}
