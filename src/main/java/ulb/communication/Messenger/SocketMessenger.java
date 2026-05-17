package ulb.communication.Messenger;

import ulb.exceptions.CommunicationException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketMessenger implements Messenger {
    private final Logger LOGGER = Logger.getLogger(SocketMessenger.class.getName());

    private final Socket socket;
    private final ObjectInputStream reader;
    private final ObjectOutputStream writer;

    public SocketMessenger(Socket socket) throws CommunicationException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket cannot be null.");
        }

        this.socket = socket;
        try{
            this.writer = new ObjectOutputStream(this.socket.getOutputStream());
            this.writer.flush();
            this.reader = new ObjectInputStream(this.socket.getInputStream());

        } catch (IOException e){
            this.close();
            throw new CommunicationException("Impossible to open network's communication.");
        }
    }

    @Override
    public void sendMessage(Serializable message) throws CommunicationException {
        if (message == null) {
            throw new IllegalArgumentException("Message cannot be null.");
        }

        try{
            this.writer.writeObject(message);
            this.writer.reset();
            this.writer.flush();
        } catch (IOException e){
            this.close();
            throw new CommunicationException("Impossible to send network message.");
        }
    }

    @Override
    public Serializable receiveMessage() throws CommunicationException {
        try{
            return (Serializable) reader.readObject();
        } catch (IOException e){
            this.close();
            throw new CommunicationException("Impossible to receive network message.");
        } catch (ClassNotFoundException e){
            this.close();
            throw new CommunicationException("Unrecognized network message.");
        }
    }

    public void close(){
        if (!socket.isClosed()){
            try{
                this.reader.close();
                this.writer.close();
                this.socket.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error while closing socket connection.");
            }
        }
    }
}
