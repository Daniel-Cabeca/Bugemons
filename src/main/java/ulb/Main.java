package ulb;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ulb.model.Player;
import ulb.view.MainMenu;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/MainMenu.fxml"));
		Parent root = loader.load();

		// Create a player instance
		Player player = new Player("Sasha");

		// Pass the player to the controller
		MainMenu controller = loader.getController();
		controller.setPlayer(player);

		Scene scene = new Scene(root);

		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
