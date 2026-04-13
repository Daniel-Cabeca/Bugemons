package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.bugemon.Bugemon;
import ulb.communication.Message;
import ulb.communication.types.GetInfoMessage;
import ulb.communication.types.InfoType;
import ulb.communication.types.RewardPlaceMessage;
import ulb.model.item.Item;
import ulb.service.ItemService;
import ulb.view.WindowPath;
import ulb.view.windows.FloorRewardWindow;

public class FloorRewardController implements FloorRewardWindow.ViewListener, ChooseBugemonController.Listener {

	public enum RewardChoice {
		STAT,
		ATTACK
	}

    private final Listener listener;
	private final GameController gameController;
	private final Stage stage;
	private FloorRewardWindow view;
	private RewardChoice pendingChoice;
    private Item rewardItem;

	public FloorRewardController(Stage stage, GameController gameController, Listener listener) {
		this.stage = stage;
		this.gameController = gameController;
		this.listener = listener;
	}

	ItemService getItemService() {
		return this.gameController.getItemService();
	}

	public void show() throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.FLOOR_REWARD));
		loader.load();
		view = loader.getController();
		view.setViewListener(this);

        rewardItem = this.getItemService().getRandomItem();

		Message m = gameController.handleMessage(new GetInfoMessage(InfoType.REWARD_PLACE));
		if (m instanceof RewardPlaceMessage placeMessage) {
			view.initializeLabels(placeMessage.getFloorNumber(), placeMessage.getRoomNumber(), rewardItem.getName());
		}


		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}

	@Override
	public void onObjectReward() {
		listener.onObjectReward(rewardItem);
	}

	@Override
	public void onChooseAttackReward() {
		pendingChoice = RewardChoice.ATTACK;
		listener.onChooseBugemonReward(pendingChoice);
	}

	@Override
	public void onStatReward() {
		pendingChoice = RewardChoice.STAT;
		listener.onChooseBugemonReward(pendingChoice);
	}

	@Override
	public void onBugemonChosen(Bugemon bugemon) {
		listener.onBugemonChosen(bugemon);
	}

	@Override
	public void onReturnFloorRewardWindow() {
		listener.onReturnFloorRewardWindow();
	}


	public interface Listener {
		void onObjectReward(Item rewardItem);
		void onChooseBugemonReward(RewardChoice rewardChoice);
		void onBugemonChosen(Bugemon bugemon);
		void onReturnFloorRewardWindow();
	}
}