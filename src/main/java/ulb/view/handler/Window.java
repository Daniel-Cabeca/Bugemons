package ulb.view.handler;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;


public abstract class Window{
	//TODO
	private Parent root;
	public void switchWindow() throws IOException{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/ulb/view/CreateTeamWindow.fxml"));
		root = loader.load();
	}
	public String getMessage(WindowName uiName, String messageType){return "";};
	public String sendMessage(WindowName uiName, String messageType){return "";};
}
