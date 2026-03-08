package ulb.view.handler;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Node;

public abstract class Window{
	public final String windowsPath= "/ulb/view/";
	public void switchWindow(ActionEvent event, String windowFxmlPath) throws IOException{
		Parent root = FXMLLoader.load(getClass().getResource(windowFxmlPath));

  		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.getScene().setRoot(root);
	}
	public String getMessage(WindowName uiName, String messageType){return "";};
	public String sendMessage(WindowName uiName, String messageType){return "";};
}
