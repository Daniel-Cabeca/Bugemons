package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.DTO.item.ItemDTO;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.ChooseItemRewardRequest;
import ulb.message.request.gameData.GetRandomItemRequest;
import ulb.message.request.gameInfo.GetTowerInfoRequest;
import ulb.message.response.gameData.RandomItemResponse;
import ulb.message.response.gameInfo.TowerInfoResponse;
import ulb.view.WindowPath;
import ulb.view.windows.FloorRewardWindow;

/**
 * Controller for picking a reward after winning a battle.
 */
public class FloorRewardController extends WindowController<FloorRewardWindow> implements FloorRewardWindow.ViewListener {
	/**
	 * Enum denoting that a stat or new ability bonus has been picked.
	 * Unset if not picked yet or if another bonus has been chosen.
	 */
	private RewardChoice pendingChoice;

	/**
	 * Item chosen as reward.
	 * Unset if not picked yet or if another bonus has been chosen.
	 */
	private ItemDTO rewardItem;

	/**
	 * Creates the floor reward controller.
	 *
	 * @param stage The application stage
	 * @param clientController The client controller
	 */
	public FloorRewardController(Stage stage, ClientController clientController) {
		super(stage, WindowPath.FLOOR_REWARD, clientController);
		this.view.setViewListener(this);
	}

	/**
	 * Displays the floor reward window, fetching a random item and tower info from the server.
	 */
	@Override
	public void show() {
		try {
			if (clientController.getData(new GetRandomItemRequest()) instanceof RandomItemResponse r) {
				rewardItem = r.getItem();
			} 
			if (clientController.getData(new GetTowerInfoRequest()) instanceof TowerInfoResponse info) {
				view.initializeLabels(info.getFloorNumber(), info.getRoomNumber(), rewardItem != null ? rewardItem.name()
						: "");
			}
		} catch (Exception e) {
			LOGGER.warning("Impossible de récupérer les récompenses ou les informations de la tour.");
		}
		super.show();
	}

	/** {@inheritDoc} */
	@Override
	public void onObjectReward() {
		if (clientController.postData(new ChooseItemRewardRequest(rewardItem))) {
			clientController.showWindow(WindowName.FLOOR);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onChooseAttackReward() {
		pendingChoice = RewardChoice.NEW_ABILITY;
		clientController.chooseBugemonReward(pendingChoice);
	}

	/** {@inheritDoc} */
	@Override
	public void onStatReward() {
		pendingChoice = RewardChoice.STAT_INCREASE;
		clientController.chooseBugemonReward(pendingChoice);
	}
}
