package ulb.communication;

import ulb.communication.Messenger.SocketMessenger;
import ulb.exceptions.CommunicationException;
import ulb.server.ClientHandler;
import ulb.service.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Server managing incoming client connections using sockets.
 */
public class SocketServer {
	/**
	 * Object used for logging runtime information to the console or to a log file.
	 */
	private static final Logger LOGGER = Logger.getLogger(SocketServer.class.getName());

	/**
	 * Socket connection with multiple clients.
	 */
	private final ServerSocket serverSocket;

	/**
	 * The threads managing each client.
	 */
	private final List<Thread> clients;

	/**
	 * Flag for closing the server.
	 */
	private boolean stopServer;

	/**
	 * Creates a socket server listening on the given port.
	 *
	 * @param port The port number to listen on
	 * @throws CommunicationException If the server socket cannot be created
	 */
	public SocketServer(int port) throws CommunicationException {
		try {
			serverSocket = new ServerSocket(port);
			this.stopServer = false;
			clients = new ArrayList<>();
			LOGGER.log(Level.INFO, "Server is running ...");
		} catch (IOException e) {
			throw new CommunicationException("Impossible to start the server on port " + port + ".");
		}
	}

	/**
	 * Starts the server loop, accepting clients and creating a handler for each.
	 *
	 * @param abilityService The ability service
	 * @param bugemonService The bugemon service
	 * @param itemService The item service
	 * @param accountService The account service
	 * @param chatService The chat service
	 * @param teamService The team service
	 * @param inventoryService The inventory service
	 * @param towerSaveService The tower save service
	 * @param multiBattleService The multiplayer battle service
	 * @throws CommunicationException If an error occurs while accepting connections
	 */
	public void start(AbilityService abilityService, BugemonService bugemonService, ItemService itemService,
					  AccountService accountService, ChatService chatService, TeamService teamService,
					  InventoryService inventoryService, TowerSaveService towerSaveService,
					  MultiBattleService multiBattleService) throws CommunicationException {
		while (!stopServer) {
			Socket clientSocket;
			if ((clientSocket = listenConnection()) != null) {
				try {
					SocketMessenger clientMessenger = new SocketMessenger(clientSocket);
					ClientHandler controller = new ClientHandler(clientMessenger, abilityService, bugemonService,
							itemService, accountService, chatService, teamService, inventoryService, towerSaveService,
							multiBattleService);
					clients.add(controller);
					LOGGER.log(Level.INFO, "Client successfully connected to the server");
					controller.start();
				} catch (CommunicationException e) {
					LOGGER.log(Level.WARNING, "Impossible to initialize communication with client.");
					closeClientSocket(clientSocket);
				}
			}
		}
		waitAllThreads();
	}

	/**
	 * Blocks until a new client connection is accepted.
	 *
	 * @return The accepted client socket, or null if the server is stopping
	 * @throws CommunicationException If an unexpected error while waiting
	 */
	private Socket listenConnection() throws CommunicationException {
		try {
			return serverSocket.accept();
		} catch (SocketException e) {
			if (stopServer || serverSocket.isClosed()) {
				return null;
			}
			close();
			throw new CommunicationException("The socket server has been interrupted.");
		} catch (IOException e) {
			close();
			throw new CommunicationException("Error while waiting for connection with client.");
		}
	}

	/**
	 * Closes a client socket if it is not already closed.
	 *
	 * @param socket The client socket to close
	 */
	private void closeClientSocket(Socket socket) {
		if (socket == null || socket.isClosed()) {
			return;
		}
		try {
			socket.close();
		} catch (IOException ignored) {
			LOGGER.log(Level.FINE, "Client socket could not be closed because it is already unusable.");
		}
	}

	/**
	 * Waits for all client threads to finish.
	 */
	public void waitAllThreads() {
		for (Thread thread : this.clients) {
			try {
				thread.join();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				close();
				return;
			}
		}
	}

	/**
	 * Stops the server and closes the server socket.
	 */
	public void close() {
		stopServer = true;
		LOGGER.log(Level.INFO, "Server has stopped successfully ...");
		if (serverSocket.isClosed()) {
			return;
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Impossible to close server.");
		}
	}
}