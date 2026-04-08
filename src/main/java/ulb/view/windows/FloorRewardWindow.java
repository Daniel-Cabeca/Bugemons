package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class FloorRewardWindow extends Window {

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
        if (viewListener != null) {
            viewListener.onObjectReward();
        }
    }

    @FXML
    private void newAttackReward(){
        if (viewListener != null) {
            viewListener.onAttackReward();
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
        void onAttackReward();
        void onStatReward();
    }
}
