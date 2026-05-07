package ulb.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;
import ulb.view.windows.GameModeWindow;

/**
 * Controller for the game mode selection.
 */
public class GameModeController implements GameModeWindow.ViewListener {
    private final Listener listener;
    private GameModeWindow view;
    private Stage stage;

    /**
     * Creates the game mode controller.
     *
     * @param stage The application stage
     * @param listener The listener notified of user actions
     */
    public GameModeController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the game mode selection view.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.GAME_MODE);
        view = loader.getController();
        view.setViewListener(this);
        boolean activateButton = listener.isTowerSaved();
        view.activateContinueTowerButton(activateButton);
        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAutoBattle() {
        listener.onAutoBattle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onControlledBattle() {
        listener.onControlledBattle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTowerMode(boolean newTower) {
        listener.onTowerMode(newTower);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturn() {
        listener.onReturnToModeWindow();
    }

    /**
     * Listener for game mode selection events.
     */
    public interface Listener {
        /** Called when auto-battle mode is selected. */
        void onAutoBattle();
        /** Called when controlled-battle mode is selected. */
        void onControlledBattle();
        /** Called when tower mode is selected. */
        void onTowerMode(boolean newTower);
        /** Called when returning to the mode selection window. */
        void onReturnToModeWindow();
        /** Returns whether a saved tower run exists for the current player. */
        boolean isTowerSaved();
    }
}