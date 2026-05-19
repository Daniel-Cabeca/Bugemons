package ulb;

import ulb.communication.SocketServer;
import ulb.repository.*;
import ulb.repository.database.*;
import ulb.repository.database.sql.Database;
import ulb.repository.database.sql.DatabaseInitializer;
import ulb.repository.inmemory.MultiBattleInMemoryRepository;
import ulb.repository.json.ItemJsonRepository;
import ulb.repository.json.StartingInventoryJsonRepository;
import ulb.service.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServer {
	private static final Logger LOGGER = Logger.getLogger(MainServer.class.getName());

	private static final String SERVER_IP = "127.0.0.1";
	private static final int SERVER_PORT = 8080;

	public static void main(String[] args) {
		String serverIp = SERVER_IP;
		Integer serverPort = SERVER_PORT;
		try {
			startServer(serverPort);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Failed to start server.");
		}
	}

	private static void startServer(Integer serverPort) throws Exception {
		SocketServer server = new SocketServer(serverPort);

		Database database = DatabaseInitializer.prepareDefaultDatabase();

		AbilityRepository abilityRepository = new AbilityDatabaseRepository(database);
		AbilityService abilityService = new AbilityService(abilityRepository);

		BugemonSpeciesRepository bugemonSpeciesRepository = new BugemonSpeciesDatabaseRepository(database);
		BugemonService bugemonService = new BugemonService(bugemonSpeciesRepository);

		ItemRepository itemRepository = new ItemDatabaseRepository(database);
		StartingInventoryRepository startingInventoryRepository =
				new StartingInventoryJsonRepository(new ItemJsonRepository());
		ItemService itemService = new ItemService(itemRepository, startingInventoryRepository);

		AccountRepository accountRepository = new AccountDatabaseRepository(database);
		AccountService accountService = new AccountService(accountRepository);

		ChatRepository chatRepository = new ChatDatabaseRepository(database);
		ChatService chatService = new ChatService(chatRepository);

		TeamRepository teamRepository = new TeamDatabaseRepository(database);
		TeamService teamService = new TeamService(teamRepository);

		InventoryRepository inventoryRepository = new InventoryDatabaseRepository(database, itemRepository);
		InventoryService inventoryService = new InventoryService(inventoryRepository);

		TowerSaveRepository towerSaveRepository = new TowerSaveDatabaseRepository(database);
		TowerSaveService towerSaveService = new TowerSaveService(towerSaveRepository);

		MultiBattleRepository multiBattleRepository = new MultiBattleInMemoryRepository();
		MultiBattleService multiBattleService = new MultiBattleService(multiBattleRepository, accountService);

		server.start(abilityService, bugemonService, itemService, accountService, chatService, teamService,
				inventoryService, towerSaveService, multiBattleService);
	}
}
