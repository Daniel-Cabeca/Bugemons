package ulb.communication.Messenger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Test;

import ulb.message.response.StatusResponse;

public class SocketMessengerTest {
    private final Logger LOGGER = Logger.getLogger(SocketMessengerTest.class.getName());

    private SocketActor server;
    private SocketActor client;

    public class SocketActor extends Thread {
        private ServerSocket connection;
        private boolean isClient;
        private Socket socket;

        private SocketMessenger messenger;

        public SocketActor(boolean isClient, ServerSocket connectionSocket){
            this.connection = connectionSocket;
            this.isClient = isClient;
        }

        public void run(){
            try{
                if (isClient){
                    this.socket = new Socket("127.0.0.1", 8080);
                } else{
                    this.socket = this.connection.accept();
                }
                this.messenger = new SocketMessenger(socket);
            } catch (Exception e){
                LOGGER.log(Level.SEVERE, "Failed to establish socket connection.", e);
            }
        }

        public Socket getSocket(){
            return this.socket;
        }

        public void sendMessage(Serializable message){
            try{
                this.messenger.sendMessage(message);
            } catch (Exception e){
                LOGGER.log(Level.WARNING, "Failed to send message.", e);
            }
        }

        public Serializable receiveMessage(){
            try{
                return this.messenger.receiveMessage();
            } catch (Exception e){
                return null;
            }
            
        }
    }

    public void createActors(){
        try{
            ServerSocket connectionSocket = new ServerSocket(8080);

            this.server = new SocketActor(false, connectionSocket);
            this.client = new SocketActor(true, connectionSocket);

            this.server.start();

            this.client.start();

            Thread.sleep(100);

            connectionSocket.close();
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "Failed to create socket actors.", e);
        }
    }

    @Test
    public void testSendingMessage(){
        this.createActors();

        StatusResponse sendingMessage = new StatusResponse(false,"Test de la communication");

        this.client.sendMessage(sendingMessage);
        StatusResponse receivingMessage = (StatusResponse) this.server.receiveMessage();

        assertNotNull(receivingMessage);
        assertEquals(sendingMessage.getMessage(), receivingMessage.getMessage());
    }

    @Test
    public void testTwoWaysSendingMessage(){
        this.createActors();
        StatusResponse sendingMessage1 = new StatusResponse(false,"Test de la communication 1");

        this.client.sendMessage(sendingMessage1);
        StatusResponse receivingMessage1 = (StatusResponse) this.server.receiveMessage();
        
        assertNotNull(receivingMessage1);
        assertEquals(sendingMessage1.getMessage(), receivingMessage1.getMessage());

        StatusResponse sendingMessage2 = new StatusResponse(false,"Test de la communication 2");

        this.server.sendMessage(sendingMessage2);
        StatusResponse receivingMessage2 = (StatusResponse) this.client.receiveMessage();
        
        assertNotNull(receivingMessage2);
        assertEquals(sendingMessage2.getMessage(), receivingMessage2.getMessage());
    }
}
