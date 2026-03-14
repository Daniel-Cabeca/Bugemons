package ulb.controller.towerManager;

import org.junit.jupiter.api.Test;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;

import static org.junit.jupiter.api.Assertions.*;

public class FloorManagerTest {

    @Test
    void floorIsNotCompletedOnCreation() {
        Floor floor = new Floor(1, false);
        FloorManager manager = new FloorManager(floor);

        assertFalse(manager.isFloorCompleted());
        assertFalse(floor.isFloorCompleted());
    }

    @Test
    void isFloorCompletedReturnsTrueWhenAllRoomsCompleted() {
        Floor floor = new Floor(1, false);
        FloorManager manager = new FloorManager(floor);

        for (Room room : floor.getRooms()) {
            room.setRoomCompleted(true);
        }

        assertTrue(manager.isFloorCompleted());
        assertTrue(floor.isFloorCompleted());
    }

    @Test
    void nextRoomAdvancesWhenCurrentRoomCompletedAndFloorNotCompleted() {
        Floor floor = new Floor(1, false);
        FloorManager manager = new FloorManager(floor);

        int beforeIndex = manager.getCurrentRoomIndex();
        Room currentRoom = manager.getCurrentRoomManager().getRoom();

        currentRoom.setRoomCompleted(true);
        manager.nextRoom();

        int afterIndex = manager.getCurrentRoomIndex();
        assertEquals(beforeIndex + 1, afterIndex);
        assertEquals(floor.getRooms().get(afterIndex), manager.getCurrentRoomManager().getRoom());
    }
}

