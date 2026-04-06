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

	private ViewListener viewListener;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	public void initializeLabels(int floorNumber, int roomNumber) {
		floorLabel.setText("Etage: NO" + floorNumber);
		roomLabel.setText("Salle: " + roomNumber);
	}

	@FXML
	private void objectReward(){
		viewListener.onObjectReward();
	}

	@FXML
	private void newAttackReward(){
		viewListener.onAttackReward();
	}

	@FXML
	private void statReward(){
		viewListener.onStatReward();
	}

	public interface ViewListener {
		void onObjectReward();
		void onAttackReward();
		void onStatReward();
	}

}
