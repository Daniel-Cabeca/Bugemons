package ulb.view.windows;

import javafx.fxml.FXML;
import ulb.view.handler.Window;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class RewardWindow extends Window {
	@FXML
	private Label bugemonLevel;
	@FXML
	private ImageView bugemonSprite;
	@FXML
	private void rewardA(){
		System.out.println("OK A!");
	}
	@FXML
	private void rewardB(){
		System.out.println("OK B!");
	}
	@FXML
	private void rewardC(){
		System.out.println("OK C!");
	}


}
