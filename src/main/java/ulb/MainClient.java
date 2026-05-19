package ulb;

import javafx.application.Application;
import ulb.controller.ClientController;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainClient {
	private static final Logger LOGGER = Logger.getLogger(MainClient.class.getName());

	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 8080;

	public static void main(String[] args) {
		String serverIp = SERVER_IP;
		Integer serverPort = SERVER_PORT;
		try {
			Application.launch(ClientController.class, serverIp, serverPort.toString());
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to launch client sided main.");
		}
	}
}
