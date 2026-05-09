package ulb.controller;
import java.util.List;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.GameMode;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.ConfirmTeamWindow;
import ulb.DTO.bugemon.BugemonDTO;

/**
 * Controller for the team confirmation window.
 */
public class ConfirmTeamController implements ConfirmTeamWindow.ViewListener {
    private final ClientController clientController;
    private ConfirmTeamWindow view;
    private Stage stage;
    private List<BugemonDTO> playerTeam;
    private GameMode gameMode;

    /**
     * Creates the confirm team controller.
     *
     * @param stage The application stage
     * @param clientController The application controller
     */
    public ConfirmTeamController(Stage stage, ClientController clientController) {
        this.stage = stage;
        this.clientController = clientController;
    }

    public void setPlayerTeam(List<BugemonDTO> playerTeam) {
        this.playerTeam = playerTeam;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Displays the team confirmation window.
     */
    public void show() throws ViewLoadException {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.CONFIRM_TEAM);
        view = loader.getController();
        view.setViewListener(this);
        //TODO: May throw error if null
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
        clientController.onReturnToCreateTeamWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onConfirm() { clientController.onConfirm(); }
}