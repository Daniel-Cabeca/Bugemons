package ulb.view.windows;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import ulb.view.WindowPath;

public class ChooseBugemonWindow extends Window{
	@FXML
	private VBox bugemonList;

	@FXML
	private void apply() {
		switchWindow(WindowPath.NEXT_ROOM);
	}

	@FXML
	private void returnFloorRewardWindow() {
		switchWindow(WindowPath.FLOOR_REWARD);
	}

}
