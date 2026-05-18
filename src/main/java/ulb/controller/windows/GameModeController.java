package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.model.GameMode;
import ulb.message.request.gameInfo.GetTowerSavedInfoRequest;
import ulb.message.request.setup.SetUpTowerModeRequest;
import ulb.message.response.gameInfo.TowerSavedInfoResponse;
import ulb.view.WindowPath;
import ulb.view.windows.GameModeWindow;
import ulb.controller.ClientController;

import java.io.Serializable;

/**
 * Controller for the game mode selection.
 */
public class GameModeController extends WindowController<GameModeWindow> implements GameModeWindow.ViewListener {

    /**
     * Creates the game mode controller.
     *
     * @param stage The application stage
     * @param clientController The client controller
     */
    public GameModeController(Stage stage, ClientController clientController) {
        super(stage, WindowPath.GAME_MODE, clientController);
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
        Serializable message = this.clientController.getData(new GetTowerSavedInfoRequest());
        if (message instanceof TowerSavedInfoResponse towerInfoMessage){
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
            if (this.clientController.postData(new SetUpTowerModeRequest(false))){
                this.clientController.showWindow(WindowName.FLOOR);
            }
        }
    }

    /**
     * Helper function that choose the gameMode and Show teamCreateWindow
     * @param gameMode indicate which game mode is chosen
     */
    private void setModeAndShowTeamWindow(GameMode gameMode){
        this.clientController.setGameMode(gameMode);
        this.clientController.showWindow(WindowName.TEAM);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturn() {
        this.clientController.showWindow(WindowName.MAIN_MENU);
    }
}