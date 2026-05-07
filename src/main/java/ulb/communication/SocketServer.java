package ulb.communication;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import ulb.communication.Messenger.SocketMessenger;
import ulb.exceptions.CommunicationException;
import ulb.server.ClientHandler;
import ulb.service.*;

public class SocketServer {
    private ServerSocket serverSocket;
    private boolean stopServer;
    private List<Thread> clients;

    public SocketServer(int port) throws CommunicationException {
        try{
            serverSocket = new ServerSocket(port);
            this.stopServer = false;
            clients = new ArrayList<Thread>();
            System.out.println("SERVER ON !");
        } catch (IOException e){
            throw new CommunicationException("Impossible to start the server on port " + port + ".", e);
        }
    }

    public void waitAllThreads(){
        for (Thread thread : this.clients){
            try{
                thread.join();
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                close();
                return;
            }
        }
    }

    private Socket listenConnection() throws CommunicationException {
        try {
            return serverSocket.accept();
        } catch (SocketException e) {
            if (stopServer || serverSocket.isClosed()) {
                return null;
            }
            close();
            throw new CommunicationException("The socket server has been interrupted.", e);
        } catch (IOException e){
            close();
            throw new CommunicationException("Error while waiting for connection with client.", e);
        }
    }

    public void start(AbilityService abilityService, BugemonService bugemonService, ItemService itemService,
    		AccountService accountService, ChatService chatService, TeamService teamService, InventoryService inventoryService, TowerSaveService towerSaveService,
            MultiBattleService multiBattleService) throws CommunicationException {
        while (!stopServer) {
            Socket clientSocket;
            if ((clientSocket = listenConnection()) != null){
                System.out.println("CLIENT ACCEPTED");

                try {
                    SocketMessenger clientMessenger = new SocketMessenger(clientSocket);
                    ClientHandler controller = new ClientHandler(clientMessenger, abilityService, bugemonService, itemService, accountService, chatService, teamService, inventoryService, towerSaveService, multiBattleService);
                    clients.add(controller);
                    controller.start();
                } catch (CommunicationException e) {
                    System.err.println("Impossible to initialize communication with client : " + e.getMessage());
                    closeClientSocket(clientSocket);
                }
            }
        }
        waitAllThreads();
        System.out.println("SERVER CLOSED !");
    }

    public void close(){
        stopServer = true;

        if (serverSocket == null || serverSocket.isClosed()) {
            return;
        }

        try{
            serverSocket.close();
        } catch (IOException e){
            System.err.println("Impossible to close server: " + e.getMessage());
        }
    }

    private void closeClientSocket(Socket socket) {
        if (socket == null || socket.isClosed()) {
            return;
        }

        try {
            socket.close();
        } catch (IOException ignored) {
            // The client socket is already unusable
        }
    }

}
