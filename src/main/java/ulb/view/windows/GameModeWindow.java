package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import ulb.utils.Scaling;

public class GameModeWindow extends Window {

    @FXML
    private VBox content;

    @FXML
    private Button continueTowerButton;

    private ViewListener viewListener;

    @FXML
    public void initialize() {
        Scaling.applyScaling(content);
    }

    public void setViewListener(ViewListener viewListener) {
        this.viewListener = viewListener;
    }

    @FXML
    public void handleAutomaticBattle() {
        viewListener.onAutoBattle();
    }

    @FXML
    public void handleControlledBattle() {
        viewListener.onControlledBattle();
    }

    @FXML
    public void handleNewTowerBattle(){
        viewListener.onTowerMode(true);
    }

    @FXML
    public void handleContinueTowerBattle(){
        viewListener.onTowerMode(false);
    }

    @FXML
    private void handleReturn(){
        viewListener.onReturn();
    }

    public void activateContinueTowerButton(boolean activateButton) {
        continueTowerButton.setDisable(!activateButton);
    }

    public interface ViewListener {
        void onAutoBattle();
        void onControlledBattle();
        void onTowerMode(boolean newTower);
        void onReturn();
    }

}
