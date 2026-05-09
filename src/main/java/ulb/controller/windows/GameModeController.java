package ulb.controller.windows;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.communication.GameMode;
import ulb.controller.ClientController;
import ulb.message.clientToServer.GetTowerSavedInfoMessage;
import ulb.message.clientToServer.SetUpTowerModeMessage;
import ulb.message.serverToClient.TowerSavedInfoMessage;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.GameModeWindow;

import java.io.Serializable;

/**
 * Controller for the game mode selection.
 */
public class GameModeController extends WindowController<GameModeWindow> implements GameModeWindow.ViewListener {

    /**
     * Creates the game mode controller.
     *
     * @param stage The application stage
     * @param clientListener listener to communicate with the clientController
     */
    public GameModeController(Stage stage, ClientListener clientListener) {
        super(stage, WindowPath.GAME_MODE, clientListener);
        this.view.setViewListener(this);
    }
    @Override
    public void show(){
        super.show();
        boolean activateButton = this.isTowerSaved();
        this.view.activateContinueTowerButton(activateButton);
    }

    private boolean isTowerSaved() {
        Serializable message = this.clientListener.onGetData(new GetTowerSavedInfoMessage());
        if (message instanceof TowerSavedInfoMessage towerInfoMessage){
            return towerInfoMessage.isTowerSaved();
        }
        return false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void onAutoBattle() {
        this.setModeAndShowTeamWindow(GameMode.AUTO);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onControlledBattle() {
        this.setModeAndShowTeamWindow(GameMode.CONTROLLED);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onTowerMode(boolean newTower) {
        if (newTower) {
            this.setModeAndShowTeamWindow(GameMode.TOWER);
        }else{
            if (this.clientListener.onPostData(new SetUpTowerModeMessage(newTower))){
                this.clientListener.onShowWindow(WindowName.FLOOR);
            }
        }
    }
    private void setModeAndShowTeamWindow(GameMode gameMode){
        this.clientListener.onSetGameMode(gameMode);
        this.clientListener.onShowWindow(WindowName.TEAM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturn() {
        this.clientListener.onShowWindow(WindowName.MODE);
    }
}