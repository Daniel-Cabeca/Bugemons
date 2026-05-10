package ulb.controller.windows;
import javafx.stage.Stage;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.message.request.gameActions.ChooseStatRewardRequest;
import ulb.message.request.gameData.GetRandomAbilityRequest;
import ulb.message.response.StatusResponse;
import ulb.message.response.gameData.RandomAbilityResponse;
import ulb.view.WindowPath;
import ulb.view.windows.ChooseBugemonWindow;

import java.io.Serializable;
import java.util.logging.Level;

/**
 * Controller for selecting a bugemon for a reward action.
 */
public class ChooseBugemonController extends WindowController<ChooseBugemonWindow> implements ChooseBugemonWindow.ViewListener {
    /**
     * Creates the choose bugemon controller.
     *
     * @param stage The application stage
     * @param clientListener The application controller
     */
    public ChooseBugemonController(Stage stage,ClientListener clientListener) {
        super(stage, WindowPath.CHOOSE_BUGEMON, clientListener);
        this.view.setViewListener(this);
    }

    /**
     * Displays the choose bugemon screen.
     */
    public void show() {
        PlayerDTO player = this.clientListener.onGetPlayer();
        this.view.populatePlayerBugemons(player.getTeam());
        super.show();
    }

    public void onBugemonChosen(BugemonDTO bugemon) {
        if (this.clientListener.onGetPendingFloorRewardChoice() == RewardChoice.STAT ) {
            if (this.clientListener.onPostData(new ChooseStatRewardRequest(bugemon))){
                this.clientListener.onShowWindow(WindowName.FLOOR);
            }
            return;
        }

        AbilityDTO newAbility = null;
        Serializable message = this.clientListener.onGetData(new GetRandomAbilityRequest(bugemon));

        if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()){
            LOGGER.log(Level.WARNING, "Failed to get random ability: " + errorMessage.getMessage());
            return;

        } else if (message instanceof RandomAbilityResponse randomAbility){
            newAbility = randomAbility.getAbility();
        }

        if (newAbility == null) {
            this.clientListener.onNextRoom();
            return;
        }
        this.clientListener.onShowAttackReplacement(bugemon, newAbility);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onReturnFloorRewardWindow() { this.clientListener.onShowWindow(WindowName.FLOOR_REWARD); }
}