package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FloorRewardWindow extends Window {

	private ViewListener viewListener;

	@FXML
	private Label floorLabel;
	@FXML
	private Label roomLabel;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	public void initializeLabels(int floorNumber, int roomNumber) {
		floorLabel.setText("Etage: NO" + floorNumber);
		roomLabel.setText("Salle: " + roomNumber);
	}

	@FXML
	private void objectReward(){
		if (viewListener != null) {
			viewListener.onObjectReward();
		}
	}

	@FXML
	private void newAttackReward(){
		if (viewListener != null) {
			viewListener.onChooseAttackReward();
		}
	}

	@FXML
	private void statReward(){
		if (viewListener != null) {
			viewListener.onChooseStatReward();
		}
	}

	public interface ViewListener {
		void onObjectReward();
		void onChooseAttackReward();
		void onChooseStatReward();
	}

}
