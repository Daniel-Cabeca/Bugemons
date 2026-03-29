package ulb;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ulb.controller.GameController;
import ulb.model.Player;
import ulb.view.ViewManager;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

import ulb.communication.Message;
import ulb.communication.types.ConnectMessage;
import ulb.communication.Server;
import ulb.communication.Client;

public class Main extends Application {
	static String SERVER_IP = "127.0.0.1";
	static int SERVER_PORT = 8080;
	public static void main(String[] args){
		try {
			if (args.length == 0){
				launch(args);
			}else if (args[0].equals("--client")){
				Client client = new Client(SERVER_IP, SERVER_PORT);

				client.sendMessage(new ConnectMessage("Bonjour server !"));

				Message message = client.receiveMessage();
				if (message instanceof ConnectMessage connectMessage){
					System.out.println("message reçu du serveur : " + connectMessage.getConnectMessage());
				}

				client.closeSocket();
			} else if (args[0].equals("--server")){
				Server server = new Server(SERVER_PORT);
				server.start();
			} else {
				System.err.println("Unknown arguments.");
			}

		} catch (Exception e) {
			System.err.println("Uncaught error.");
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		GameController gameController = new GameController();
		Player player = new Player("Player");
		gameController.setPlayer(player);

		ViewManager viewManager = new ViewManager() {};
		viewManager.setGameController(gameController);
		viewManager.setStage(primaryStage);
		gameController.setViewManager(viewManager); // possible change

		Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);

		viewManager.switchWindow(WindowPath.MODE);

		primaryStage.getScene().getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());

		primaryStage.show();
	}
}