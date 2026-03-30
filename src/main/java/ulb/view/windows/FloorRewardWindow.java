package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.communication.Message;
import ulb.communication.types.GetInfoMessage;
import ulb.communication.types.InfoType;
import ulb.communication.types.RewardPlaceMessage;
import ulb.controller.towerManager.TowerManager;
import ulb.view.WindowPath;

public class FloorRewardWindow extends Window {
	//TODO: implement functions + add popup window to newAttackReward and StatReward to select bugemon

	@FXML
	private Button objectReward;
	@FXML
	private Button newAttackReward;
	@FXML
	private Button statReward;
	@FXML
	private Label floorLabel;
	@FXML
	private Label roomLabel;

	public void initializeLabels(int floorNumber, int roomNumber) {
		floorLabel.setText("Etage: NO" + floorNumber);
		roomLabel.setText("Salle: " + roomNumber);
	}

	@Override
	public void onLoad() {
		Message m = viewManager.handleMessage(new GetInfoMessage(InfoType.REWARD_PLACE));
		if (m instanceof RewardPlaceMessage placeMessage) {
			initializeLabels(placeMessage.getFloorNumber(), placeMessage.getRoomNumber());
		}
	}

	@FXML
	private void objectReward(ActionEvent event){
		sendSwitchWindowMessage(WindowPath.NEXT_ROOM);
	}
	@FXML
	private void newAttackReward(ActionEvent event){
		sendSwitchWindowMessage(WindowPath.NEXT_ROOM);
	}
	@FXML
	private void statReward(ActionEvent event){
		sendSwitchWindowMessage(WindowPath.NEXT_ROOM);
	}

}
