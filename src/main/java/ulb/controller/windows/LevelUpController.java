package ulb.controller.windows;

import javafx.event.ActionEvent;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.controller.ClientController;
import ulb.exceptions.UnknownServerResponse;
import ulb.message.request.gameInfo.GetLevelUpInfoRequest;
import ulb.message.response.gameInfo.LevelUpInfoResponse;
import ulb.view.WindowPath;
import ulb.view.windows.LevelUpWindow;

import java.util.List;

/**
 * Controller for the level up reward selection screen.
 */
public class LevelUpController extends WindowController<LevelUpWindow> implements LevelUpWindow.ViewListener {

	/**
	 * Creates the level up controller.
	 *
	 * @param stage The application stage
	 * @param clientController The client controller
	 */
	public LevelUpController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.LEVEL_UP, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * Displays the level up screen, fetching available rewards from the server.
	 */
	@Override
	public void show() {
		List<RewardDTO> rewards;
		try {
			if (clientController.getData(new GetLevelUpInfoRequest()) instanceof LevelUpInfoResponse info) {
				rewards = info.getRewards();
			} else {
				throw new UnknownServerResponse("getLevelUpInfo");
			}
		} catch (Exception e) {
			LOGGER.warning("Impossible de récupérer les récompenses de montée de niveau.");
			return;
		}
		if (rewards == null || rewards.isEmpty()) return;
		BugemonDTO bugemon = rewards.get(0).bugemon();
		view.initializeView(bugemon, rewards);
		super.show();
	}

	/** 
	 * {@inheritDoc} 
	 */
	@Override
	public void onRewardChosen(RewardDTO reward, ActionEvent event) {
		clientController.chooseReward(reward);
	}
}
