package ulb.view.handler;

import javafx.fxml.FXML;


public abstract class Window{
	@FXML
	public void initialize(){};
	public String getMessage(WindowName uiName, String messageType){return "";};
	public String sendMessage(WindowName uiName, String messageType){return "";};
}
