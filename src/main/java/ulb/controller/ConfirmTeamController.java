package ulb.controller;

import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.GameMode;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;
import ulb.view.windows.ConfirmTeamWindow;

import ulb.DTO.bugemon.BugemonDTO;

/**
 * Controller for the battle mode selection screen.
 */
public class ConfirmTeamController implements ConfirmTeamWindow.ViewListener {

    private final Listener listener;
    private ConfirmTeamWindow view;
    private Stage stage;
    private List<BugemonDTO> playerTeam;
    private GameMode gameMode;

    public ConfirmTeamController(Stage stage, Listener listener, List<BugemonDTO> playerTeam, GameMode gameMode) {
        this.stage = stage;
        this.listener = listener;
        this.playerTeam = playerTeam;
        this.gameMode = gameMode;
    }

    /**
     * Displays the battle mode selection view.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.CONFIRM_TEAM);
        view = loader.getController();
        view.setViewListener(this);
        view.setGameModeLabel(gameMode);
        view.displayTeam(playerTeam);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    /**
     * Handles returning to the previous screen.
     */
    @Override
    public void onReturn() {
        listener.onReturnToCreateTeamWindow();
    }

    @Override
    public void onConfirm() { listener.onConfirm(); }

    /**
     * Listener for battle mode selection events.
     */
    public interface Listener {
        /** Called when returning to the create team screen. */
        void onReturnToCreateTeamWindow();
        void onConfirm();
    }
}
