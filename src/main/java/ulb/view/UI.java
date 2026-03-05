package ulb.view;

import javafx.fxml.FXML;


public abstract class UI{
	@FXML
	public void initialize(){};
	public String getMessage(UIName uiName, String messageType){return "";};
	public String sendMessage(UIName uiName, String messageType){return "";};
}
