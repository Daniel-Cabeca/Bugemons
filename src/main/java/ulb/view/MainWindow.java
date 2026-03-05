package ulb.view;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;


public class MainWindow extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/MainWindow.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setScene(scene);
		primaryStage.setFullScreen(true);
	 	scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
		primaryStage.show();
	}
}
