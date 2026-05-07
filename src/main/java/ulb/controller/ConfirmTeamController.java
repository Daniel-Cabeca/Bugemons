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
 * Controller for the team confirmation window.
 */
public class ConfirmTeamController implements ConfirmTeamWindow.ViewListener {
    private final Listener listener;
    private ConfirmTeamWindow view;
    private Stage stage;
    private List<BugemonDTO> playerTeam;
    private GameMode gameMode;

    /**
     * Creates the confirm team controller.
     *
     * @param stage The application stage
     * @param listener The listener notified of user actions
     * @param playerTeam The list of bugemons in the player's team
     * @param gameMode The selected game mode
     */
    public ConfirmTeamController(Stage stage, Listener listener, List<BugemonDTO> playerTeam, GameMode gameMode) {
        this.stage = stage;
        this.listener = listener;
        this.playerTeam = playerTeam;
        this.gameMode = gameMode;
    }

    /**
     * Displays the team confirmation window.
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
     * {@inheritDoc}
     */
    @Override
    public void onReturn() {
        listener.onReturnToCreateTeamWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfirm() { listener.onConfirm(); }

    /**
     * Listener for team confirmation events.
     */
    public interface Listener {
        /** Called when returning to the create team window. */
        void onReturnToCreateTeamWindow();
        /** Called when the player confirms their team. */
        void onConfirm();
    }
}