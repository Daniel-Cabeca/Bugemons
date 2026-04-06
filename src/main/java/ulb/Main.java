package ulb;

import javafx.application.Application;
import ulb.communication.Server;
import ulb.controller.ClientController;

public class Main{
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args){
        String serverIp = SERVER_IP;
        Integer serverPort = SERVER_PORT;
        try{
            if (args.length == 0){
                Application.launch(ClientController.class, serverIp, serverPort.toString());

            } else if ("--server".equals(args[0])){
                Server server = new Server(serverPort);
                server.start();
            }
        } catch (Exception e){
            System.err.println(e);
        }
    } 
}
