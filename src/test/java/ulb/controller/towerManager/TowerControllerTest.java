package ulb.controller.towerManager;

import org.junit.jupiter.api.Test;

import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import  ulb.model.tower.Tower;

import static org.junit.jupiter.api.Assertions.*;

public class TowerControllerTest {


	@Test
	void towerIsNotCompletedOnCreation() {
		TowerManager manager = new TowerManager();

		assertFalse(manager.isTowerCompleted(), "New tower should not be completed");

		// also check the internal flag on Tower
		Tower tower = manager.getTower();
		assertFalse(tower.getTowerCompleted());
	}

	@Test
	void isTowerCompletedReturnsTrueWhenAllFloorsCompleted() {
		TowerManager manager = new TowerManager();

		Tower tower = manager.getTower();
		// mark all floors as completed
		tower.getFloors().forEach(f -> f.setFloorCompleted(true));

		assertTrue(manager.isTowerCompleted(), "Tower should be completed");
		assertTrue(tower.getTowerCompleted(), "Tower internal flag should be true");
	}

	@Test
	void nextFloorAdvancesWhenCurrentFloorCompletedAndTowerNotCompleted() throws Exception {
		TowerManager manager = new TowerManager();

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
		TowerManager manager = new TowerManager();
		Tower tower = manager.getTower();
		// mark all floors as completed
		tower.getFloors().forEach(f -> f.setFloorCompleted(true));

		int before = manager.getFloorNumber();
		manager.nextFloor(); // condition !isTowerCompleted() should fail

		int after = manager.getFloorNumber();
		assertEquals(before, after, "Floor index should not change when tower is completed");
	}
}
