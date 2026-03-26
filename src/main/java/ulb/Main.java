package ulb;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ulb.controller.GameController;
import ulb.model.Player;
import ulb.view.ViewManager;
import ulb.view.WindowPath;
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

		ViewManager viewManager = new ViewManager() {};
		viewManager.setGameController(gameController);
		viewManager.setStage(primaryStage);
		gameController.setViewManager(viewManager); // possible change

		Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);

		viewManager.switchWindow(WindowPath.MODE);

		primaryStage.getScene().getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());

		primaryStage.show();
	}
}
