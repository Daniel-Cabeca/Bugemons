package ulb.communication.Messenger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

import ulb.communication.types.ErrorMessage;
import ulb.communication.Message;

public class SocketMessengerTest {
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
                System.err.println(e);
            }
        }

        public Socket getSocket(){
            return this.socket;
        }

        public void sendMessage(Message message){
            try{
                this.messenger.sendMessage(message);
            } catch (Exception e){
                System.err.println(e);
            }
        }

        public Message receiveMessage(){
            try{
                return this.messenger.receiveMessage();
            } catch (Exception e){
                System.err.println(e);
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
            System.err.println(e);
        }
    } 

    @Test
    public void testConnection(){
        this.createActors();
        assertTrue(true);
    }

    @Test
    public void testSendingMessage(){
        this.createActors();
        ErrorMessage sendingMessage = new ErrorMessage("Test de la communication");

        this.client.sendMessage(sendingMessage);
        ErrorMessage receivingMessage = (ErrorMessage) this.server.receiveMessage();

        assertNotNull(receivingMessage);
        assertEquals(sendingMessage.getError(), receivingMessage.getError());
    }

    @Test
    public void testTwoWaysSendingMessage(){
        this.createActors();
        ErrorMessage sendingMessage1 = new ErrorMessage("Test de la communication 1");

        this.client.sendMessage(sendingMessage1);
        ErrorMessage receivingMessage1 = (ErrorMessage) this.server.receiveMessage();
        
        assertNotNull(receivingMessage1);
        assertEquals(sendingMessage1.getError(), receivingMessage1.getError());

        ErrorMessage sendingMessage2 = new ErrorMessage("Test de la communication 2");

        this.server.sendMessage(sendingMessage2);
        ErrorMessage receivingMessage2 = (ErrorMessage) this.client.receiveMessage();
        
        assertNotNull(receivingMessage2);
        assertEquals(sendingMessage2.getError(), receivingMessage2.getError());
    }
}
