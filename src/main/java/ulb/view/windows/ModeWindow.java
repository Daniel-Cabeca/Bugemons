package ulb.view.windows;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import ulb.view.handler.Window;

final public class ModeWindow extends Window{

	@FXML
	private void goSoloMode() {
		try{
			switchWindow();
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
