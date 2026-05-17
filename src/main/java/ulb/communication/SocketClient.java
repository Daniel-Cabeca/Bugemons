package ulb.communication;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;

import ulb.communication.Messenger.SocketMessenger;
import ulb.exceptions.CommunicationException;
import ulb.message.request.Request;
import ulb.message.response.Response;
import ulb.message.response.StatusResponse;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketClient {
    private final Object serverRequestLock = new Object();

    private final Socket socket;
    private final SocketMessenger messenger;
    private static final Logger LOGGER = Logger.getLogger(SocketClient.class.getName());

    public SocketClient(String serverIP, int serverPort) throws CommunicationException {
        try{
            socket = new Socket(serverIP, serverPort);
            messenger = new SocketMessenger(socket);
            LOGGER.log(Level.INFO, "Client successfully connected to the server");
        } catch (IOException e){
            closeSocket();
            throw new CommunicationException("Impossible to connect to server.");
        } catch (CommunicationException e){
            closeSocket();
            throw e;
        }
    }

    /**
     * Send a request to the server.
     *
     * @param request The request to send
     * @throws CommunicationException If a network error occurs
     */
    void sendRequest(Request request) throws CommunicationException {
        if (messenger == null) {
            throw new CommunicationException("Network with client has not been initialized.");
        }

        messenger.sendMessage(request);
    }

    /**
     * Waits for and receives a response from the server.
     * To be used after a request has been sent.
     *
     * @return The response from the server.
     * @throws CommunicationException If a network error occurs or if the response received is not an instance of Response
     */
    Response receiveResponse() throws CommunicationException {
        if (messenger == null) {
            throw new CommunicationException("Network with client has not been initialized.");
        }

        Serializable responseSerializable = messenger.receiveMessage();

        if (responseSerializable instanceof Response response) {
            return response;
        }

        throw new CommunicationException("Received object that is not an instance of Response from the server.");
    }

    /**
     * Sends a request, waits for the server's response and returns it.
     * If an error occurs, returns a StatusResponse set as failure.
     *
     * @param request The request to send
     * @return The response from the server, or a StatusResponse set to failure in case of an error
     */
    public Response getResponse(Request request) {
        synchronized (this.serverRequestLock) {
            try {
                this.sendRequest(request);
                return this.receiveResponse();
            } catch (CommunicationException e) {
                return new StatusResponse(false, e.getMessage());
            }
        }
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
