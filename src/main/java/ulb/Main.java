package ulb;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ulb.model.Loader;
import ulb.model.Player;
import ulb.view.MainMenu;
import ulb.model.battle.BattleSnapshot;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/MainMenu.fxml"));
		Parent root = loader.load();

		// Create a player instance
		Player player = new Player("Player");

		MainMenu controller = loader.getController();
		controller.setPlayer(player);

		Scene scene = new Scene(root);

		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
	 	scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());


		primaryStage.show();
	}

	public static void main(String[] args) {
		try {
			mainBody(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void mainBody(String[] args) throws Exception {
		Loader.load();
		launch(args);
	}
}
