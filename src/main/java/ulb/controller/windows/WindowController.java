package ulb.controller.windows;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.communication.GameMode;
import ulb.controller.ClientController;
import ulb.message.request.Request;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Abstract class that represent a WindowController
 *
 * @param <T> ViewWindow of the windowController
 */
public abstract class WindowController<T> {
    protected Stage stage;
    protected T view;
    protected FXMLLoader loader;
    protected final Logger LOGGER = Logger.getLogger(this.getClass().getName());
    protected ClientListener clientListener;
    protected ClientController clientController;

    /**
     * A super constructor for all windowControlls
     * @param stage used to show fxml windows
     * @param windowPath the fxml path of the viewWindow
     * @param clientController the client controller
     */
    protected WindowController(Stage stage, String windowPath, ClientController clientController){
        this.stage = stage;
        this.clientListener = clientController;
        this.clientController = clientController;

        try {
            this.loadView(windowPath);
        } catch (ViewLoadException e){
            LOGGER.log(Level.WARNING, "\u001B[31m" + "Impossible d'afficher l'écran de " + this.getClass().getName() + "\u001B[0m");
        }
    }

    /**
     * Read the fxml file and load the view window
     */
    protected void loadView(String windowPath) throws ViewLoadException {
        loader = FxmlLoader.load(this, windowPath);
        view = loader.getController();
    }

    /**
     * Changes the fxml window to the current fxml window
     */
    public void show() {
        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.centerOnScreen(); //keeps UI neat
        this.stage.show();
    }

    /**
     * ClientListener is a way to communicate between WindowControllers and the ClientController
     */
    public interface ClientListener{
        Serializable onGetData(Request message);
        boolean onPostData(Request message);
        PlayerDTO onGetPlayer();
        void onSetPlayer(PlayerDTO player);
        PlayerDTO onLoadPlayer(String userName);
        void onSetOpponentMulti(PlayerDTO opponent);
		void onShowWindow(WindowName window);
        void onSetGameMode(GameMode gameMode);
        /**
         * Set newTimeLine in WaitWindow
         * @param waitCycle is current waitCycle
         */
        void onSetNewTimeLine(EventHandler waitCycle);
        void onStopWaitWindow();
		void setupTeamAndShowConfirmTeam(List<BugemonDTO> teamDTO);

        // Tower flow — complex routing that must stay in ClientController
        void onRoomSelectionComplete();
        void onChooseBugemonReward(RewardChoice rewardChoice);
        void onRewardChosen(RewardDTO reward, ActionEvent event);
        void onNextRoom();
        void onShowAttackReplacement(BugemonDTO bugemon, AbilityDTO newAbility);
        RewardChoice onGetPendingFloorRewardChoice();
    }

    /**
     * WindowName enum is used by showWindow function to show the right window
     * Each name represent an FXML ViewWindow to show
     */
    public enum WindowName{
        REGISTER, MODE,GAME_MODE, ATTACK_REPLACEMENT, BATTLE, BATTLE_END,
        CHOOSE_BUGEMON,CONFIRM_TEAM,FLOOR, FLOOR_REWARD, LEVEL_UP, LOAD_TEAM_PANEL,
        NEXT_ROOM, SOCIAL_PANEL, TEAM, WAIT
    }
}
