package ulb.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.GameModeWindow;

/**
 * Controller for the game mode selection.
 */
public class GameModeController implements GameModeWindow.ViewListener {
    private final ClientController clientController;
    private GameModeWindow view;
    private Stage stage;

    /**
     * Creates the game mode controller.
     *
     * @param stage The application stage
     * @param clientController The application controller
     */
    public GameModeController(Stage stage, ClientController clientController) {
        this.stage = stage;
        this.clientController = clientController;
    }

    /**
     * Displays the game mode selection view.
     */
    public void show() throws ViewLoadException {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.GAME_MODE);
        view = loader.getController();
        view.setViewListener(this);
        boolean activateButton = clientController.isTowerSaved();
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
        clientController.onAutoBattle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onControlledBattle() {
        clientController.onControlledBattle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTowerMode(boolean newTower) {
        clientController.onTowerMode(newTower);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturn() {
        clientController.switchToModeWindow();
    }
}