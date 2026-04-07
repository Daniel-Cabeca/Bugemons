package ulb.communication.Messenger;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ulb.communication.Message;

public class SocketMessenger implements Messenger {
    private Socket socket;
    private ObjectInputStream reader;
    private ObjectOutputStream writer;

    public SocketMessenger(Socket socket){
        this.socket = socket;
        try{

            this.writer = new ObjectOutputStream(this.socket.getOutputStream());
            this.reader = new ObjectInputStream(this.socket.getInputStream());
            
        } catch (Exception e){
            this.close();
            System.err.println(e);
        }
    }

    public void sendMessage(Message message) throws Exception{
        try{
            this.writer.writeObject(message);
            this.writer.reset();
        } catch (Exception e){
            this.close();
            throw e;
        }
    }

    public Message receiveMessage() throws Exception{
        Message message = null;
        try{
            message = (Message) reader.readObject();
        } catch (Exception e){
            this.close();
            throw e;
        }
        return message;
    }

    public void close(){
        if (!socket.isClosed()){
            try{
                this.socket.close();
                this.reader.close();
                this.writer.close();
            } catch (Exception e){
                System.err.println(e);
            }
        }
    }
}
