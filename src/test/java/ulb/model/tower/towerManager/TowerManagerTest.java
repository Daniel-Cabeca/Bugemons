package ulb.model.tower.towerManager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import  ulb.model.tower.Tower;
import ulb.repository.BugemonSpeciesRepository;
import ulb.repository.ItemRepository;
import ulb.repository.StartingInventoryRepository;
import ulb.repository.TowerSaveRepository;
import ulb.repository.database.AccountDatabaseRepository;
import ulb.repository.database.BugemonSpeciesDatabaseRepository;
import ulb.repository.database.ItemDatabaseRepository;
import ulb.repository.database.TeamDatabaseRepository;
import ulb.repository.database.TowerSaveDatabaseRepository;
import ulb.repository.database.sql.DatabaseMock;
import ulb.repository.json.ItemJsonRepository;
import ulb.repository.json.StartingInventoryJsonRepository;
import ulb.repository.mock.BugemonSpeciesMockRepository;
import ulb.repository.mock.StartingInventoryMockRepository;import ulb.repository.mock.ItemMockRepository;
import ulb.service.AccountService;
import ulb.service.BugemonService;import ulb.service.ItemService;
import ulb.service.TeamService;
import ulb.service.TowerSaveService;

public class TowerManagerTest {

	private Bugemon makeBugemon(BugemonService bugemonService) {
		return bugemonService.spawnBugemon("florachu");
	}

	private TowerManager setTowerManager() throws Exception {
		DatabaseMock database = new DatabaseMock();
		BugemonService bugemonService = new BugemonService(new BugemonSpeciesDatabaseRepository(database));
		ItemService itemService = new ItemService(new ItemDatabaseRepository(database), new StartingInventoryJsonRepository(new ItemJsonRepository()));
		TowerSaveService towerSaveService = new TowerSaveService(new TowerSaveDatabaseRepository(database));
		TeamService teamService = new TeamService(new TeamDatabaseRepository(database));
		AccountService accountService = new AccountService(new AccountDatabaseRepository(database));
		
		accountService.register("player", "pwd");
		Player player = new Player("player", accountService.getUserId("player"));
		Bugemon a = makeBugemon(bugemonService);
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);

		return new TowerManager(player, bugemonService, itemService, teamService, towerSaveService);
	}

	@Test
	void towerIsNotCompletedOnInitialisation() throws Exception {

		TowerManager manager = setTowerManager();

		assertFalse(manager.isTowerCompleted(), "New tower should not be completed");

		// also check the internal flag on Tower
		Tower tower = manager.getTower();
		assertFalse(tower.getTowerCompleted());
	}

	@Test
	void towerIsCompletedWhenAllFloorsCompleted() throws Exception {

		TowerManager manager = setTowerManager();

		Tower tower = manager.getTower();
		// mark all floors as completed
		tower.getFloors().forEach(f -> f.setFloorCompleted(true));

		assertTrue(manager.isTowerCompleted(), "Tower should be completed");
		assertTrue(tower.getTowerCompleted(), "Tower internal flag should be true");
	}

	@Test
	void advanceFloorWhenCurrentFloorIsCompletedAndTowerIsNotCompleted()throws Exception {

		TowerManager manager = setTowerManager();

		int before = manager.getFloorNumber();
		FloorManager floorManager = manager.getCurrentFloorManager();
		Floor currentFloor = floorManager.getFloor();

		// mark all rooms in current floor as completed
		for (Room room : currentFloor.getRooms()) {
			room.setRoomCompleted(true);
		}

		manager.nextFloor();

		int after = manager.getFloorNumber();
		assertEquals(before + 1, after, "Floor index should advance by 1");
		assertEquals(currentFloor.getId() + 1,
				manager.getCurrentFloorManager().getFloor().getId(),
				"Current floor manager should now point to the next floor");
	}

	@Test
	void doesNotAdvanceFloorWhenTowerIsCompleted() throws Exception {
		
		TowerManager manager = setTowerManager();

		Tower tower = manager.getTower();
		// mark all floors as completed
		tower.getFloors().forEach(f -> f.setFloorCompleted(true));

		int before = manager.getFloorNumber();
		manager.nextFloor(); // condition !isTowerCompleted() should fail

		int after = manager.getFloorNumber();
		assertEquals(before, after, "Floor index should not change when tower is completed");
	}
}
