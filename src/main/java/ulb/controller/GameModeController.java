package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;
import ulb.view.windows.GameModeWindow;


public class GameModeController implements GameModeWindow.ViewListener {

    private final Listener listener;
    private GameModeWindow view;
    private Stage stage;

    public GameModeController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    /**
     * Displays the battle mode selection view.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.GAME_MODE);
        view = loader.getController();
        view.setViewListener(this);
        view.activateContinueTowerButton(listener.isTowerSaved());


        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    /**
     * Handles auto-battle mode selection.
     */
    @Override
    public void onAutoBattle() {
        listener.onAutoBattle();
    }

    /**
     * Handles controlled-battle mode selection.
     */
    @Override
    public void onControlledBattle() {
        listener.onControlledBattle();
    }

    /**
     * Handles tower mode selection.
     */
    @Override
    public void onTowerMode(boolean newTower) {
        listener.onTowerMode(newTower);
    }

    /**
     * Handles returning to the previous screen.
     */
    @Override
    public void onReturn() {
        listener.onReturnToModeWindow();
    }

    public interface Listener {
        /** Called when auto-battle mode is selected. */
        void onAutoBattle();
        /** Called when controlled-battle mode is selected. */
        void onControlledBattle();
        /** Called when tower mode is selected. */
        void onTowerMode(boolean newTower);
        void onReturnToModeWindow();
        boolean isTowerSaved();
    }

}
