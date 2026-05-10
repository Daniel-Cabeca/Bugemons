package ulb.controller.windows;
import javafx.stage.Stage;
import ulb.communication.GameMode;
import ulb.message.clientToServer.gameInfo.GetTowerSavedInfoMessage;
import ulb.message.clientToServer.setup.SetUpTowerModeMessage;
import ulb.message.serverToClient.gameInfo.TowerSavedInfoMessage;
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

    /**
     * show GameModeWindow and enable or disable TowerButton
     */
    @Override
    public void show(){
        super.show();
        boolean activateButton = this.isTowerSaved();
        this.view.activateContinueTowerButton(activateButton);
    }

    /**
     * Helper function that checks towerSave in database
     * @return true if there is a tower Saving database else false
     */
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
            if (this.clientListener.onPostData(new SetUpTowerModeMessage(false))){
                this.clientListener.onShowWindow(WindowName.FLOOR);
            }
        }
    }

    /**
     * Helper function that choose the gameMode and Show teamCreateWindow
     * @param gameMode indicate which game mode is chosen
     */
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