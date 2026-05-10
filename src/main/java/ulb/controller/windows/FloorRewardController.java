package ulb.controller.windows;

import java.util.List;

import javafx.stage.Stage;
import ulb.DTO.item.ItemDTO;
import ulb.message.clientToServer.gameActions.ChooseItemRewardMessage;
import ulb.message.clientToServer.gameData.GetRandomItemMessage;
import ulb.message.serverToClient.gameData.RandomItemMessage;
import ulb.message.clientToServer.gameInfo.GetTowerInfoMessage;
import ulb.message.serverToClient.gameInfo.TowerInfoMessage;
import ulb.view.WindowPath;
import ulb.view.windows.FloorRewardWindow;

public class FloorRewardController extends WindowController<FloorRewardWindow> implements FloorRewardWindow.ViewListener {

    private RewardChoice pendingChoice;
    private ItemDTO rewardItem;

    /**
     * Creates the floor reward controller.
     *
     * @param stage The application stage
     * @param clientListener Listener to communicate with the ClientController
     */
    public FloorRewardController(Stage stage, ClientListener clientListener) {
        super(stage, WindowPath.FLOOR_REWARD, clientListener);
        this.view.setViewListener(this);
    }

    /**
     * Displays the floor reward window, fetching a random item and tower info from the server.
     */
    @Override
    public void show() {
        if (clientListener.onGetData(new GetRandomItemMessage()) instanceof RandomItemMessage r) {
            rewardItem = r.getItem();
        }
        if (clientListener.onGetData(new GetTowerInfoMessage()) instanceof TowerInfoMessage info) {
            view.initializeLabels(info.getFloorNumber(), info.getRoomNumber(), rewardItem != null ? rewardItem.name() : "");
        }
        super.show();
    }

    /** {@inheritDoc} */
    @Override
    public void onObjectReward() {
        if (clientListener.onPostData(new ChooseItemRewardMessage(rewardItem))) {
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
