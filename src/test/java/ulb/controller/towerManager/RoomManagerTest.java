package ulb.controller.towerManager;

import org.junit.jupiter.api.Test;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;

import static org.junit.jupiter.api.Assertions.*;

public class RoomManagerTest {

    @Test
    void roomIsInitiallyNotCompleted() {
        Room room = new Room(1, RoomType.BATTLE);
        RoomManager manager = new RoomManager(room);

        assertFalse(manager.isRoomCompleted());
        assertFalse(room.isRoomCompleted());
    }

    @Test
    void setRoomCompletedUpdatesUnderlyingRoom() {
        Room room = new Room(1, RoomType.REWARD);
        RoomManager manager = new RoomManager(room);

        manager.setRoomCompleted(true);

        assertTrue(manager.isRoomCompleted());
        assertTrue(room.isRoomCompleted());
    }
}

