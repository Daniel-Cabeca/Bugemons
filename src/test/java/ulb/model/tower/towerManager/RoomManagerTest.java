package ulb.model.tower.towerManager;

import org.junit.jupiter.api.Test;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.mock.ItemMockRepository;
import ulb.repository.mock.StartingInventoryMockRepository;
import ulb.service.BugemonService;
import ulb.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomManagerTest {

	@Test
	void roomNotCompletedOnInitialisation() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		Room room = new Room(1, RoomType.BATTLE);
		RoomManager manager = new RoomManager(room, 1, player, bugemonService, itemService);

		assertFalse(manager.isRoomCompleted());
		assertFalse(room.isRoomCompleted());
	}

	private Bugemon makeBugemon() throws LoadException, EntityNotFoundException {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
		BugemonService bugemonService = new BugemonService(bugemonRepository);

		return bugemonService.spawnBugemon("florachu");
	}

	@Test
	void setRoomCompletedUpdatesManagerAndRoom() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		Room room = new Room(1, RoomType.REWARD);
		RoomManager manager = new RoomManager(room, 1, player, bugemonService, itemService);

		manager.setRoomCompleted(true);

		assertTrue(manager.isRoomCompleted());
		assertTrue(room.isRoomCompleted());
	}

	@Test
	void createBattleRoomInitializesBattle() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		// give the player a minimal valid team
		Team team = new Team(List.of(makeBugemon()));
		player.setTeam(team);

		Room room = new Room(1, RoomType.BATTLE);
		RoomManager manager = new RoomManager(room, 1, player, bugemonService, itemService);

		assertNotNull(manager.getBattle(), "Battle controller should be initialized for a BATTLE room");
	}

	@Test
	void createBossRoomInitializesBossBattle() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		// give the player a minimal valid team
		Team team = new Team(List.of(makeBugemon()));
		player.setTeam(team);

		Room room = new Room(1, RoomType.BOSS);
		RoomManager manager = new RoomManager(room, 1, player, bugemonService, itemService);

		assertNotNull(manager.getBattle(), "Battle controller should be initialized for a BATTLE room");
		assertTrue(manager.getBattle().isBossBattle());
	}
}

