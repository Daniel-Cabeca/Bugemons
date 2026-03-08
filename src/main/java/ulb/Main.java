package ulb;
import javafx.application.Application;
import ulb.controller.BattleController;
import ulb.view.windows.MainWindow;

public class Main {
	public static void main(String[] args){
		new BattleController();
		Application.launch(MainWindow.class,args);
	}
}
