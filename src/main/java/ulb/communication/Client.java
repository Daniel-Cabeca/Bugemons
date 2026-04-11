package ulb.communication;

import java.io.Serializable;
import java.net.Socket;

import ulb.communication.Messenger.SocketMessenger;

public class Client {
    private Socket socket;
    private SocketMessenger messenger;

    public Client(String serverIP, int serverPort){
        try{
            socket = new Socket(serverIP, serverPort);
            System.out.println("CONNECTED TO SERVER");

            messenger = new SocketMessenger(socket);
            
        } catch (Exception e){
            System.err.println(e);
        }
    }

    public void sendMessage(Message message){
        try{
            messenger.sendMessage(message);
        } catch (Exception e){
            System.err.println(e);
        }
    }

    public Serializable receiveMessage(){
        try{
            return messenger.receiveMessage();
        } catch (Exception e){
            System.err.println(e);
        }
        return null;
    }

    public void closeSocket(){
        messenger.close();
    }
}   
