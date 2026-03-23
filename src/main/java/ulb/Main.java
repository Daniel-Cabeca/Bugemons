package ulb;
import javafx.application.Application;
import ulb.controller.BattleController;
import ulb.view.windows.MainWindow;
import ulb.App.App;

public class Main {
	public static void main(String[] args){
		try {
			App.run(args);
		} catch (Exception e) {
			System.err.println("Uncaught error.");
			System.err.println(e.getMessage());
		}
	}
}
