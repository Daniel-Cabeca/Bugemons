package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class FloorRewardWindow {

	private ViewListener viewListener;

	@FXML
	private Label floorLabel;
	@FXML
	private Label roomLabel;
	@FXML
	private Button objectReward;

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	public void initializeLabels(int floorNumber, int roomNumber, String item) {
		floorLabel.setText("Etage: NO" + floorNumber);
		roomLabel.setText("Salle: " + roomNumber);
		objectReward.setText("Objet\n" + item);
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
			viewListener.onStatReward();
		}
	}

    public interface ViewListener {
        void onObjectReward();
        void onChooseAttackReward();
        void onStatReward();
    }
}
