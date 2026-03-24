package ulb.App;

import javafx.application.Application;
import ulb.controller.GameController;
import ulb.model.Player;
import ulb.view.windows.MainWindow;

public class App {

	public static void run(String[] args){
		GameController gameController = new GameController();
		Player player = new Player("Player");
		gameController.setPlayer(player);
		MainWindow.setGameController(gameController);
		Application.launch(MainWindow.class,args);


    }
}
