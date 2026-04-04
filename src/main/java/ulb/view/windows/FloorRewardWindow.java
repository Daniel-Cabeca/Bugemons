package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ulb.communication.Message;
import ulb.communication.types.GetInfoMessage;
import ulb.communication.types.InfoType;
import ulb.communication.types.ReceiveObjectRewardMessage;
import ulb.communication.types.RewardPlaceMessage;
import ulb.view.WindowPath;

public class FloorRewardWindow extends Window {
	//TODO: implement functions + add popup window to newAttackReward and StatReward to select bugemon

	@FXML
	private Label floorLabel;
	@FXML
	private Label roomLabel;

	@Override
	public void onLoad() {
		Message m = sendMessage(new GetInfoMessage(InfoType.REWARD_PLACE));
		if (m instanceof RewardPlaceMessage placeMessage) {
			initializeLabels(placeMessage.getFloorNumber(), placeMessage.getRoomNumber());
		}
	}

	public void initializeLabels(int floorNumber, int roomNumber) {
		floorLabel.setText("Etage: NO" + floorNumber);
		roomLabel.setText("Salle: " + roomNumber);
	}

	@FXML
	private void objectReward(){
		sendMessage(new ReceiveObjectRewardMessage());
	}

	@FXML
	private void newAttackReward(){
		switchWindow(WindowPath.NEXT_ROOM);
	}

	@FXML
	private void statReward(){
		switchWindow(WindowPath.NEXT_ROOM);
	}

}
