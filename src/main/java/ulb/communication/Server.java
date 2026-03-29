package ulb.communication;

import java.net.ServerSocket;
import java.net.Socket;

import ulb.communication.Messenger.SocketMessenger;
import ulb.communication.types.ConnectMessage;

public class Server {
    ServerSocket serverSocket;
    
    public Server(int port){
        try{
            serverSocket = new ServerSocket(port);
        } catch (Exception e){
            System.err.println(e);
        }
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

    public void start(){
        while (true) {
            Socket clientSocket;
            if ((clientSocket = listenConnection()) != null){
                System.out.println("CLIENT ACCEPTED");
                SocketMessenger clientMessenger = new SocketMessenger(clientSocket);

                Message message = clientMessenger.receiveMessage();
                if (message instanceof ConnectMessage connectMessage){
                    System.out.println("message reçu de client : " + connectMessage.getConnectMessage());
                }

                clientMessenger.SendMessage(new ConnectMessage("Bonjour client !"));
                
                clientMessenger.close();
                this.close();

                return;
            }
        }
    }

    public void close(){
        try{
            serverSocket.close();
        } catch (Exception e){
            return;
        }
    }

}
