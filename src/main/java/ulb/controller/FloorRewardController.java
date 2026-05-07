package ulb.controller;

import java.util.List;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.view.FxmlLoader;
import ulb.exceptions.ViewLoadException;
import ulb.view.WindowPath;
import ulb.view.windows.FloorRewardWindow;

public class FloorRewardController implements FloorRewardWindow.ViewListener, ChooseBugemonController.Listener {

	/**
	 * Represents the type of bugemon reward selected by the player.
	 */
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

	public void show() throws ViewLoadException {
		FXMLLoader loader = FxmlLoader.load(this, WindowPath.FLOOR_REWARD);
		view = loader.getController();
		view.setViewListener(this);

        rewardItem = listener.getRandomItem();

		List<Integer> towerInfo = listener.getTowerInfo();
		if (towerInfo != null){
			view.initializeLabels(towerInfo.get(0), towerInfo.get(1), rewardItem.name());
		}

		Parent root = loader.getRoot();
		if (stage.getScene() == null) {
			stage.setScene(new Scene(root));
		} else {
			stage.getScene().setRoot(root);
		}
		this.stage.show();
	}

	/**
	 * Handles object reward selection.
	 */
	@Override
	public void onObjectReward() {
		listener.onObjectReward(rewardItem);
	}

	/**
	 * Handles attack reward selection and requests bugemon choice.
	 */
	@Override
	public void onChooseAttackReward() {
		pendingChoice = RewardChoice.ATTACK;
		listener.onChooseBugemonReward(pendingChoice);
	}

	/**
	 * Handles stat reward selection and requests bugemon choice.
	 */
	@Override
	public void onStatReward() {
		pendingChoice = RewardChoice.STAT;
		listener.onChooseBugemonReward(pendingChoice);
	}

	/**
	 * Handles bugemon selection for the pending reward.
	 *
	 * @param bugemon The selected bugemon
	 */
	@Override
	public void onBugemonChosen(BugemonDTO bugemon) {
		listener.onBugemonChosen(bugemon);
	}

	/**
	 * Handles return action from the choose bugemon screen.
	 */
	@Override
	public void onReturnFloorRewardWindow() {
		listener.onReturnFloorRewardWindow();
	}

	/**
	 * Listener for floor reward actions.
	 */
	public interface Listener {
		void onObjectReward(ItemDTO rewardItem);
		void onChooseBugemonReward(RewardChoice rewardChoice);
		void onBugemonChosen(BugemonDTO bugemon);
		void onReturnFloorRewardWindow();
		ItemDTO getRandomItem();
		List<Integer> getTowerInfo();
	}
}
