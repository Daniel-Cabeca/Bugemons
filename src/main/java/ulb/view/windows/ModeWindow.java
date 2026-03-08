package ulb.view.windows;


import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ulb.view.handler.Window;

final public class ModeWindow extends Window{
	public final String nextWindow= "CreateTeamWindow.fxml";

	@FXML
	private void goSoloMode(ActionEvent event) {
		try{
			switchWindow(event, windowsPath+nextWindow);
		}
		catch (IOException e){
			throw new RuntimeException("Cannot load FXML", e);
		}
	}

	@FXML
	private void quit(){
		Platform.exit();
		System.exit(0);
	}
}
