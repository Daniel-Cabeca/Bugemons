package ulb;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import ulb.repository.database.AccountDatabaseRepository;
import ulb.repository.database.DatabaseManager;
import ulb.controller.GameController;
import ulb.model.Player;
import ulb.service.AccountService;
import ulb.view.ViewManager;
import ulb.view.WindowPath;

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

				client.sendMessage(new ConnectMessage("Hello server !"));

				Message message = client.receiveMessage();
				if (message instanceof ConnectMessage connectMessage){
					System.out.println("Message received from the server : " + connectMessage.getConnectMessage());
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

		DatabaseManager dbManager = new DatabaseManager();
		AccountDatabaseRepository accountRepo = new AccountDatabaseRepository(dbManager.getConnection());
		AccountService accountService = new AccountService(accountRepo);

		GameController gameController = new GameController();
		Player player = new Player("Player");
		gameController.setPlayer(player);

		ViewManager viewManager = new ViewManager() {};
		viewManager.setGameController(gameController);
		viewManager.setAccountService(accountService);
		viewManager.setStage(primaryStage);
		gameController.setViewManager(viewManager);

		Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");

		viewManager.switchWindow(WindowPath.LOGIN);

		primaryStage.getScene().getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());

		primaryStage.show();
	}
}
