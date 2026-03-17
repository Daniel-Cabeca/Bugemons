package ulb.view.windows;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;import javafx.stage.Stage;

public class MainWindow extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/ModeWindow.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
	 	scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
		primaryStage.show();
	}
}
