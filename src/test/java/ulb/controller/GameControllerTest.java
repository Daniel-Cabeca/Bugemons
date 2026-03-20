package ulb.controller;

import org.junit.jupiter.api.Test;
import ulb.controller.towerManager.FloorManager;
import ulb.controller.towerManager.TowerManager;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

	private static class TestableGameController extends GameController {
		boolean towerBattleWindowCalled = false;

		public TestableGameController(TowerManager towerManager) {
			super(towerManager);
		}

		@Override
		public void switchToTowerBattleWindow(Team teamA, javafx.event.ActionEvent event) {
			towerBattleWindowCalled = true;
		}
	}

	@Test
	void handleTowerCompletesRoomAndAdvancesToNextRoom() {
		Player player = new Player("TestPlayer");
		Team team = new Team(List.of(new Bugemon(10, 10, 10, 10)));
		player.setTeam(team);

		TowerManager towerManager = new TowerManager(player);
		FloorManager floorManager = towerManager.getCurrentFloorManager();

		assertEquals(RoomType.BATTLE,
				floorManager.getCurrentRoomManager().getRoom().getRoomType(),
				"First room of the tower should be a BATTLE room");

		TestableGameController controller = new TestableGameController(towerManager);

		int initialFloorNumber = towerManager.getFloorNumber();
		int initialRoomIndex = floorManager.getCurrentRoomIndex();

		controller.handleTower(team, null);

		assertTrue(controller.towerBattleWindowCalled,
				"Battle window should be opened for a BATTLE room");

		Room firstRoom = floorManager.getFloor().getRooms().get(initialRoomIndex);
		assertTrue(firstRoom.isRoomCompleted(),
				"Current room should be marked as completed after handling tower");

		assertEquals(initialRoomIndex + 1, floorManager.getCurrentRoomIndex(),
				"FloorManager should advance to the next room");

		assertEquals(initialFloorNumber, towerManager.getFloorNumber(),
				"TowerManager should not advance to the next floor yet");
	}
}
