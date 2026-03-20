package ulb.controller.towerManager;

import org.junit.jupiter.api.Test;

import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.sample.BugemonSample;
import ulb.model.team.Team;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import  ulb.model.tower.Tower;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TowerManagerTest {


	@Test
	void towerIsNotCompletedOnCreation() {
		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		TowerManager manager = new TowerManager(player);

		assertFalse(manager.isTowerCompleted(), "New tower should not be completed");

		// also check the internal flag on Tower
		Tower tower = manager.getTower();
		assertFalse(tower.getTowerCompleted());
	}

	@Test
	void isTowerCompletedReturnsTrueWhenAllFloorsCompleted() {
		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		TowerManager manager = new TowerManager(player);

		Tower tower = manager.getTower();
		// mark all floors as completed
		tower.getFloors().forEach(f -> f.setFloorCompleted(true));

		assertTrue(manager.isTowerCompleted(), "Tower should be completed");
		assertTrue(tower.getTowerCompleted(), "Tower internal flag should be true");
	}

	@Test
	void nextFloorAdvancesWhenCurrentFloorCompletedAndTowerNotCompleted() throws Exception {

		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		TowerManager manager = new TowerManager(player);
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
	void nextFloorDoesNotAdvanceWhenTowerAlreadyCompleted() throws Exception {
		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
		TowerManager manager = new TowerManager(player);
		Tower tower = manager.getTower();
		// mark all floors as completed
		tower.getFloors().forEach(f -> f.setFloorCompleted(true));

		int before = manager.getFloorNumber();
		manager.nextFloor(); // condition !isTowerCompleted() should fail

		int after = manager.getFloorNumber();
		assertEquals(before, after, "Floor index should not change when tower is completed");
	}
}
