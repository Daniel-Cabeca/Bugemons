package ulb.controller.windows;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.message.request.gameInfo.GetLevelUpInfoRequest;
import ulb.message.response.gameInfo.LevelUpInfoResponse;
import ulb.view.WindowPath;
import ulb.view.windows.LevelUpWindow;

/**
 * Controller for the level up reward selection screen.
 */
public class LevelUpController extends WindowController<LevelUpWindow> implements LevelUpWindow.ViewListener {

    /**
     * Creates the level up controller.
     *
     * @param stage The application stage
     * @param clientListener Listener to communicate with the ClientController
     */
    public LevelUpController(Stage stage, ClientListener clientListener) {
        super(stage, WindowPath.LEVEL_UP, clientListener);
        this.view.setViewListener(this);
    }

    /**
     * Displays the level up screen, fetching available rewards from the server.
     */
    @Override
    public void show() {
        if (!(clientListener.onGetData(new GetLevelUpInfoRequest()) instanceof LevelUpInfoResponse info)) return;
        List<RewardDTO> rewards = info.getRewards();
        if (rewards == null || rewards.isEmpty()) return;
        BugemonDTO bugemon = rewards.get(0).bugemon();
        view.initializeView(bugemon, rewards);
        super.show();
    }

    /** {@inheritDoc} */
    @Override
    public void onRewardChosen(RewardDTO reward, ActionEvent event) {
        clientListener.onRewardChosen(reward, event);
    }
}
