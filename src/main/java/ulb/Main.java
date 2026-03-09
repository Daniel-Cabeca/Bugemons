package ulb;
import javafx.application.Application;
import ulb.controller.BattleController;
import ulb.view.windows.MainWindow;

public class Main {
	public static void main(String[] args){
		try {
			Application.launch(MainWindow.class,args);
		} catch (Exception e) {
			System.err.println("Uncaught error.");
			System.err.println(e.getMessage());
		}
	}
}
