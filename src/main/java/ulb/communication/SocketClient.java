package ulb.communication;

import java.io.Serializable;
import java.net.Socket;

import ulb.communication.Messenger.SocketMessenger;
import ulb.message.ClientToServerMessage;

public class SocketClient {
    private Socket socket;
    private SocketMessenger messenger;

    public SocketClient(String serverIP, int serverPort){
        try{
            socket = new Socket(serverIP, serverPort);
            System.out.println("CONNECTED TO SERVER");

            messenger = new SocketMessenger(socket);
            
        } catch (Exception e){
            System.err.println(e);
        }
    }

    public void sendMessage(ClientToServerMessage message){
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
