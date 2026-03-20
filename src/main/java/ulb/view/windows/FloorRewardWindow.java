package ulb.view.windows;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import ulb.controller.GameController;
import ulb.controller.towerManager.TowerManager;

public class FloorRewardWindow {
	//TODO: implement functions + add popup window to newAttackReward and StatReward to select bugemon

	private GameController gameController;
	private TowerManager towerManager;

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
	public void setGameController(GameController gameController){ this.gameController = gameController;}
	public void setTowerManager(TowerManager towerManager){ this.towerManager = towerManager;}

	public void initializeLabels() {
		floorLabel.setText("Etage: NO" + towerManager.getFloorNumber());
		roomLabel.setText("Salle: " + towerManager.getCurrentRoomIndex());
	}

	// for now simply switches to next room window (no reward gained)
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
