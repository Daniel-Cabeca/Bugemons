package ulb.controller;

import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
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
	private final Stage stage;
	private FloorRewardWindow view;
	private RewardChoice pendingChoice;
    private ItemDTO rewardItem;

	public FloorRewardController(Stage stage, Listener listener) {
		this.stage = stage;
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

        rewardItem = listener.getRandomItem();
		
		List<Integer> towerInfo = listener.getTowerInfo();
		if (towerInfo != null){
			view.initializeLabels(towerInfo.get(0), towerInfo.get(1), rewardItem.getName());
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
	public void onBugemonChosen(BugemonDTO bugemon) {
		listener.onBugemonChosen(bugemon);
	}

	@Override
	public void onReturnFloorRewardWindow() {
		listener.onReturnFloorRewardWindow();
	}


	public interface Listener {
		void onObjectReward(ItemDTO rewardItem);
		void onChooseBugemonReward(RewardChoice rewardChoice);
		void onBugemonChosen(BugemonDTO bugemon);
		void onReturnFloorRewardWindow();
		ItemDTO getRandomItem();
		List<Integer> getTowerInfo();
	}
}
