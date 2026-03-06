package ulb.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

import ulb.model.Player;

public class MainMenu {

	private Player player;

	public void setPlayer(Player player) {
		this.player = player;
	}

	@FXML
	private void handleCreateTeam(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/CreateTeamMenu.fxml"));
		Parent root = loader.load();

		CreateTeamWindow controller = loader.getController();
		controller.setPlayer(player);
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}

	@FXML
	private void handleExit() {
		Platform.exit();
		System.exit(0);
	}
}
