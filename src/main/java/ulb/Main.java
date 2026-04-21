package ulb;

import javafx.application.Application;
import ulb.communication.SocketServer;
import ulb.controller.ClientController;

import ulb.repository.*;
import ulb.repository.database.*;
import ulb.repository.database.sql.*;
import ulb.repository.json.ItemJsonRepository;
import ulb.repository.json.InventoryJsonRepository;
import ulb.service.*;

public class Main{
    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 8080;

    public static void main(String[] args){
        String serverIp = SERVER_IP;
        Integer serverPort = SERVER_PORT;
        try{
            if (args.length == 0){
                Application.launch(ClientController.class, serverIp, serverPort.toString());

            } else if ("--server".equals(args[0])){
				startServer(serverPort);
            }
        } catch (Exception e){
            System.err.println(e);
        }
    }

	private static void startServer(Integer serverPort) {
		SocketServer server = new SocketServer(serverPort);

		Database database = DatabaseInitializer.prepareDefaultDatabase();

		AbilityRepository abilityRepository = new AbilityDatabaseRepository(database);
		AbilityService abilityService = new AbilityService(abilityRepository);

		BugemonSpeciesRepository bugemonSpeciesRepository = new BugemonSpeciesDatabaseRepository(database);
		BugemonService bugemonService = new BugemonService(bugemonSpeciesRepository);

		ItemRepository itemRepository = new ItemDatabaseRepository(database);
		InventoryRepository inventoryRepository = new InventoryJsonRepository(new ItemJsonRepository());
		ItemService itemService = new ItemService(itemRepository, inventoryRepository);

		AccountRepository accountRepository = new AccountDatabaseRepository(database);
		AccountService accountService = new AccountService(accountRepository);

		ChatRepository chatRepository = new ChatDatabaseRepository(database);
		ChatService chatService = new ChatService(chatRepository);

		TeamRepository teamRepository = new TeamDatabaseRepository(database);
		TeamService teamService = new TeamService(teamRepository);

		server.start(abilityService, bugemonService, itemService, accountService, chatService, teamService);
	}
}
