package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.controller.GameController;

public class FloorRewardWindow {
	//TODO: implement functions + add popup window to newAttque and BonusStat to select bugemon
	private GameController gameController;

	public void setGameController(GameController gameController){ this.gameController = gameController;}

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

	private void switchToNextRoomWindow(ActionEvent event){
		gameController.switchToNextRoomWindow(event);
	}

	@FXML
	private void objectReward(ActionEvent event){
		switchToNextRoomWindow(event);
	}
	@FXML
	private void newAttackReward(ActionEvent event){
		switchToNextRoomWindow(event);
	}
	@FXML
	private void statReward(ActionEvent event){
		switchToNextRoomWindow(event);
	}

}
