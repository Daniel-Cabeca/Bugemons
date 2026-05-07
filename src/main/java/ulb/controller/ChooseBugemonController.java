package ulb.controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.ChooseBugemonWindow;

/**
 * Controller for selecting a bugemon for a reward action.
 */
public class ChooseBugemonController implements ChooseBugemonWindow.ViewListener {
    private final Listener listener;
    private final Stage stage;
    private final PlayerDTO player;
    private ChooseBugemonWindow view;

    /**
     * Creates the choose bugemon controller.
     *
     * @param stage The application stage
     * @param listener The listener notified of user actions
     * @param player The current player data
     */
    public ChooseBugemonController(Stage stage, Listener listener, PlayerDTO player) {
        this.stage = stage;
        this.listener = listener;
        this.player = player;
    }

    /**
     * Displays the choose bugemon screen.
     */
    public void show() throws ViewLoadException {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.CHOOSE_BUGEMON);
        view = loader.getController();
        view.setViewListener(this);
        view.populatePlayerBugemons(player.getTeam());
        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        stage.show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onBugemonChosen(BugemonDTO bugemon) {
        listener.onBugemonChosen(bugemon);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturnFloorRewardWindow() {
        listener.onReturnFloorRewardWindow();
    }

    /**
     * Listener for choose bugemon actions.
     */
    public interface Listener {
        /** Handles the selection of a bugemon as a reward. */
        void onBugemonChosen(BugemonDTO bugemon);
        /** Handles returning to the floor reward window. */
        void onReturnFloorRewardWindow();
    }
}