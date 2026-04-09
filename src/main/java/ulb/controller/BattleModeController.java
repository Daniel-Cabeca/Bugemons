package ulb.controller;

import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.BattleModeWindow;

import ulb.DTO.bugemon.BugemonDTO;

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

    @Override
    public void onAutoBattle() {
        listener.onAutoBattle();
    }

    @Override
    public void onControlledBattle() {
        listener.onControlledBattle();
    }

    @Override
    public void onTowerMode() {
        listener.onTowerMode();
    }

    @Override
    public void onReturn() {
        listener.onReturnToCreateTeamWindow();
    }

    public interface Listener {
        void onAutoBattle();
        void onControlledBattle();
        void onTowerMode();
        void onReturnToCreateTeamWindow();
    }
}
