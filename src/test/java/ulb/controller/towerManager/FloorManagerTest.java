package ulb.controller.towerManager;

import org.junit.jupiter.api.Test;
import ulb.model.Player;
import ulb.model.bugemon.Bugemon;
import ulb.model.sample.BugemonSample;
import ulb.model.team.Team;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FloorManagerTest {

    @Test
    void floorIsNotCompletedOnInitialisation() {
		Player player = new Player("TestPlayer");
        Floor floor = new Floor(1, false);
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        FloorManager manager = new FloorManager(floor, player);

        assertFalse(manager.isFloorCompleted());
        assertFalse(floor.isFloorCompleted());
    }

    @Test
    void floorIsCompletedWhenAllRoomsCompleted() {
		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Floor floor = new Floor(1, false);
        FloorManager manager = new FloorManager(floor, player);

        for (Room room : floor.getRooms()) {
            room.setRoomCompleted(true);
        }

        assertTrue(manager.isFloorCompleted());
        assertTrue(floor.isFloorCompleted());
    }

    @Test
    void AdvanceToNextRoomWhenCurrentRoomCompletedAndFloorNotCompleted() {
		Player player = new Player("TestPlayer");
		Bugemon a = BugemonSample.getA();
		Team teamA = new Team(List.of(a));
		player.setTeam(teamA);
        Floor floor = new Floor(1, false);
        FloorManager manager = new FloorManager(floor, player);

        int beforeIndex = manager.getCurrentRoomIndex();
        Room currentRoom = manager.getCurrentRoomManager().getRoom();

        currentRoom.setRoomCompleted(true);
        manager.nextRoom();

        int afterIndex = manager.getCurrentRoomIndex();
        assertEquals(beforeIndex + 1, afterIndex);
        assertEquals(floor.getRooms().get(afterIndex), manager.getCurrentRoomManager().getRoom());
    }
}

