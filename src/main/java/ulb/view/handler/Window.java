package ulb.view.handler;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Node;

public abstract class Window{
	protected static final String WINDOWS_PATH= "/ulb/view/";
	protected static final String MODE_WINDOW_PATH = WINDOWS_PATH+"ModeWindow.fxml";

	public void switchWindow(ActionEvent event, String windowFxmlPath) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource(windowFxmlPath));

  		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}
}
