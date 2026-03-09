package ulb;
import javafx.application.Application;
import ulb.view.windows.MainWindow;

import ulb.model.Loader;

public class Main {
	public static void main(String[] args){
		try {
			Loader.load();
			Application.launch(MainWindow.class,args);
		} catch (Exception e) {
			System.err.println("Uncaught error.");
			System.err.println(e.getMessage());
		}
	}
}
