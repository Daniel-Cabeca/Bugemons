package ulb;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ulb.controller.GameController;
import ulb.model.Player;
import ulb.view.windows.ModeWindow;

public class Main extends Application {
	public static void main(String[] args){
		try {
			launch(args);
		} catch (Exception e) {
			System.err.println("Uncaught error.");
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GameController gameController = new GameController();
		Player player = new Player("Player");
		gameController.setPlayer(player);

		Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/ModeWindow.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		ModeWindow modeWindowController = loader.getController();
		modeWindowController.setGameController(gameController);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
		scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
		primaryStage.show();
	}
}
