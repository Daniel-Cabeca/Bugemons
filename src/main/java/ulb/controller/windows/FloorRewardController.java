package ulb.controller.windows;

import javafx.stage.Stage;
import ulb.DTO.item.ItemDTO;
import ulb.controller.ClientController;
import ulb.message.request.gameActions.ChooseItemRewardRequest;
import ulb.message.request.gameData.GetRandomItemRequest;
import ulb.message.response.gameData.RandomItemResponse;
import ulb.message.request.gameInfo.GetTowerInfoRequest;
import ulb.message.response.gameInfo.TowerInfoResponse;
import ulb.view.WindowPath;
import ulb.view.windows.FloorRewardWindow;

public class FloorRewardController extends WindowController<FloorRewardWindow> implements FloorRewardWindow.ViewListener {

    private RewardChoice pendingChoice;
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
        if (clientListener.onGetData(new GetRandomItemRequest()) instanceof RandomItemResponse r) {
            rewardItem = r.getItem();
        }
        if (clientListener.onGetData(new GetTowerInfoRequest()) instanceof TowerInfoResponse info) {
            view.initializeLabels(info.getFloorNumber(), info.getRoomNumber(), rewardItem != null ? rewardItem.name() : "");
        }
        super.show();
    }

    /** {@inheritDoc} */
    @Override
    public void onObjectReward() {
        if (clientListener.onPostData(new ChooseItemRewardRequest(rewardItem))) {
            clientListener.onShowWindow(WindowName.FLOOR);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onChooseAttackReward() {
        pendingChoice = RewardChoice.ATTACK;
        clientListener.onChooseBugemonReward(pendingChoice);
    }

    /** {@inheritDoc} */
    @Override
    public void onStatReward() {
        pendingChoice = RewardChoice.STAT;
        clientListener.onChooseBugemonReward(pendingChoice);
    }
}
