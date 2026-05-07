package ulb.communication;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import ulb.communication.Messenger.SocketMessenger;
import ulb.exceptions.CommunicationException;
import ulb.message.ClientToServerMessage;

public class SocketClient {
    private Socket socket;
    private SocketMessenger messenger;

    public SocketClient(String serverIP, int serverPort) throws CommunicationException {
        try{
            socket = new Socket(serverIP, serverPort);
            System.out.println("CONNECTED TO SERVER");

            messenger = new SocketMessenger(socket);

        } catch (IOException e){
            closeSocket();
            throw new CommunicationException("Impossible to connext to server.", e);
        } catch (CommunicationException e){
            closeSocket();
            throw e;
        }
    }

    public void sendMessage(ClientToServerMessage message) throws CommunicationException {
        if (messenger == null) {
            throw new CommunicationException("Network with client has not been initialized.");
        }

        messenger.sendMessage(message);
    }

    public Serializable receiveMessage() throws CommunicationException {
        if (messenger == null) {
            throw new CommunicationException("Network with client has not been initialized.");
        }

        return messenger.receiveMessage();
    }

    public void closeSocket(){
        if (messenger != null) {
            messenger.close();
            return;
        }

        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ignored) {
                // The client is already being closed
            }
        }
    }
}
