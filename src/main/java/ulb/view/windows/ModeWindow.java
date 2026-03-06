package ulb.view.windows;

import java.awt.event.ActionEvent;
import java.io.IOException;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Node;



final public class ModeWindow {
	@FXML
	private void goSoloMode(ActionEvent event) throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/CreateTeamWindow.fxml"));
		Parent root = loader.load();
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}

	@FXML
	private void quit(){
		Platform.exit();
		System.exit(0);
	}
}
