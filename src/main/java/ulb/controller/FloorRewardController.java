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

/**
 * Controller for floor reward selection in tower mode.
 */
public class FloorRewardController implements FloorRewardWindow.ViewListener, ChooseBugemonController.Listener {

	/**
	 * Represents the type of bugemon reward selected by the player.
	 */
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

	/**
	 * Creates the floor reward controller.
	 *
	 * @param stage The application stage
	 * @param gameController The game controller used for data retrieval
	 * @param listener The listener notified of reward actions
	 */
	public FloorRewardController(Stage stage, GameController gameController, Listener listener) {
		this.stage = stage;
		this.gameController = gameController;
		this.listener = listener;
	}

	/**
	 * Returns the item service used to pick rewards.
	 *
	 * @return The item service
	 */
	ItemService getItemService() {
		return this.gameController.getItemService();
	}

	/**
	 * Displays the floor reward screen.
	 *
	 * @throws Exception If the FXML cannot be loaded
	 */
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
	public void onBugemonChosen(Bugemon bugemon) {
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
		/**
		 * Called when an item reward is selected.
		 *
		 * @param rewardItem The selected reward item
		 */
		void onObjectReward(Item rewardItem);
		/**
		 * Called when a bugemon-based reward is selected.
		 *
		 * @param rewardChoice The selected reward type
		 */
		void onChooseBugemonReward(RewardChoice rewardChoice);
		/**
		 * Called when a bugemon is selected for reward application.
		 *
		 * @param bugemon The selected bugemon
		 */
		void onBugemonChosen(Bugemon bugemon);
		/** Called when returning to the floor reward window. */
		void onReturnFloorRewardWindow();
	}
}