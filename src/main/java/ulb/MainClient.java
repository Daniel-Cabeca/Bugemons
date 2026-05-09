package ulb;

import javafx.application.Application;
import ulb.controller.ClientController;

public class MainClient {
	private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8080;

	public static void main(String[] args){
        String serverIp = SERVER_IP;
        Integer serverPort = SERVER_PORT;
        try{
            Application.launch(ClientController.class, serverIp, serverPort.toString());
        } catch (Exception e){
            System.err.println(e);
        }
    }
}
