package ulb.controller;

import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.FxmlLoader;
import ulb.view.WindowPath;
import ulb.view.windows.BattleModeWindow;

import ulb.DTO.bugemon.BugemonDTO;

/**
 * Controller for the battle mode selection screen.
 */
public class BattleModeController implements BattleModeWindow.ViewListener {

    private final Listener listener;
    private BattleModeWindow view;
    private Stage stage;
    private List<BugemonDTO> playerTeam;

    public BattleModeController(Stage stage, Listener listener, List<BugemonDTO> playerTeam) {
        this.stage = stage;
        this.listener = listener;
        this.playerTeam = playerTeam;
    }

    /**
     * Displays the battle mode selection view.
     */
    public void show() {
        FXMLLoader loader = FxmlLoader.load(this, WindowPath.BATTLE_MODE);
        view = loader.getController();
        view.setViewListener(this);
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
        listener.onReturnToCreateTeamWindow();
    }

    /**
     * Listener for battle mode selection events.
     */
    public interface Listener {
        /** Called when auto-battle mode is selected. */
        void onAutoBattle();
        /** Called when controlled-battle mode is selected. */
        void onControlledBattle();
        /** Called when tower mode is selected. */
        void onTowerMode(boolean newTower);
        /** Called when returning to the create team screen. */
        void onReturnToCreateTeamWindow();
    }
}
