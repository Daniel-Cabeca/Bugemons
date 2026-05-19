package ulb.model.tower.towerManager;

import org.junit.jupiter.api.Test;
import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.mock.ItemMockRepository;
import ulb.repository.mock.StartingInventoryMockRepository;
import ulb.service.BugemonService;
import ulb.service.ItemService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FloorManagerTest {

	@Test
	void floorIsNotCompletedOnInitialisation() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		Floor floor = new Floor(1, false);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);

		assertFalse(manager.isFloorCompleted());
		assertFalse(floor.isFloorCompleted());
	}

	private Bugemon makeBugemon() throws LoadException, EntityNotFoundException {
		BugemonSpeciesRepository bugemonRepository = new BugemonSpeciesMockRepository();
		BugemonService bugemonService = new BugemonService(bugemonRepository);

		return bugemonService.spawnBugemon("florachu");
	}

	@Test
	void floorIsCompletedWhenAllRoomsCompleted() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		Floor floor = new Floor(1, false);
		FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);

		for (Room room : floor.getRooms()) {
			room.setRoomCompleted(true);
		}

		assertTrue(manager.isFloorCompleted());
		assertTrue(floor.isFloorCompleted());
	}

	@Test
	void doesntMoveToNextRoomIfPreviousNotCompleted() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		Floor floor = new Floor(1, false);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);
		// move to allowed room first
		manager.moveToRoom(5);
		// try to move to next room while previous is not completed (5 and 6 are adjacent)
		manager.moveToRoom(6);
		assertNotEquals(6, manager.getCurrentRoomId());

	}

	@Test
	void doesntMoveToNextRoomIfPreviousNotAdjacent() throws Exception {
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesMockRepository());
		ItemService itemService = new ItemService(new ItemMockRepository(), new StartingInventoryMockRepository());

		Player player = new Player();
		Floor floor = new Floor(1, false);
		Bugemon a = makeBugemon();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		FloorManager manager = new FloorManager(floor, player, bugemonService, itemService);
		manager.moveToRoom(5);
		manager.getRoom().setRoomCompleted(true);
		// rooms 5 and 7 aren't adjacent
		manager.moveToRoom(7);
		assertNotEquals(6, manager.getCurrentRoomId());
	}
}

