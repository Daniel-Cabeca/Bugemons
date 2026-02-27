package ulb.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu {

	@FXML
	private void handleCreateTeam(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/ulb/view/CreateTeamMenu.fxml"));
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}

	@FXML
	private void handleExit() {
		Platform.exit();
		System.exit(0);
	}
}
