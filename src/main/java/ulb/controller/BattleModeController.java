package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.team.Team;
import ulb.view.WindowPath;
import ulb.view.windows.BattleModeWindow;

/**
 * Controller for the battle mode selection screen.
 */
public class BattleModeController implements BattleModeWindow.ViewListener {

    private final Listener listener;
    private BattleModeWindow view;
    private Stage stage;
    private Team playerTeam;

    /**
     * Creates the controller with dependencies for battle mode selection.
     *
     * @param stage The application stage
     * @param listener The listener notified of mode selections
     * @param playerTeam The player's current team
     */
    public BattleModeController(Stage stage, Listener listener, Team playerTeam) {
        this.stage = stage;
        this.listener = listener;
        this.playerTeam = playerTeam;
    }

    /**
     * Displays the battle mode selection view.
     *
     * @throws Exception If the FXML cannot be loaded
     */
    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.BATTLE_MODE));
        loader.load();
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
    public void onTowerMode() {
        listener.onTowerMode();
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
        void onTowerMode();
        /** Called when returning to the create team screen. */
        void onReturnToCreateTeamWindow();
    }
}
