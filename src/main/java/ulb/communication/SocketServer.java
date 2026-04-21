package ulb.communication;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import ulb.communication.Messenger.SocketMessenger;
import ulb.server.ClientHandler;
import ulb.service.*;

public class SocketServer {
    private ServerSocket serverSocket;
    private boolean stopServer;
    private List<Thread> clients;
    
    public SocketServer(int port){
        try{
            serverSocket = new ServerSocket(port);
            this.stopServer = false;
            clients = new ArrayList<Thread>();
            System.out.println("SERVER ON !");
        } catch (Exception e){
            System.err.println(e);
        }
    }

    public void waitAllThreads(){
        for (Thread thread : this.clients){
            try{
                thread.join();
            } catch (Exception e){
                System.err.println(e);
            }
        }
        return;
    }

    private Socket listenConnection(){
        try {
            Socket clientSocket;

            while ((clientSocket = serverSocket.accept()) == null) {
                Thread.sleep(100);
            }
            return clientSocket;

        } catch (Exception e){
            this.close();
            System.err.println(e);
        }
        return null;
    }

    public void start(AbilityService abilityService, BugemonService bugemonService, ItemService itemService,
    		AccountService accountService, ChatService chatService, TeamService teamService){
        while (!stopServer) {
            Socket clientSocket;
            if ((clientSocket = listenConnection()) != null){
                System.out.println("CLIENT ACCEPTED");
                SocketMessenger clientMessenger = new SocketMessenger(clientSocket);

                ClientHandler controller = new ClientHandler(clientMessenger, abilityService, bugemonService, itemService, accountService, chatService, teamService);
                clients.add(controller);
                controller.start();
            }
        }
        waitAllThreads();
        System.out.println("SERVER CLOSED !");
    }

    public void close(){
        try{
            serverSocket.close();
        } catch (Exception e){
            return;
        }
    }

}
