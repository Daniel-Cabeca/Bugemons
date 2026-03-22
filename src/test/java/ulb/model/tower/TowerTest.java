package ulb.model.tower;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TowerTest {

    @Test
    public void amountOfFloorsInTowerOnInitialisation() {
        Tower tower = new Tower();
        assertEquals(9, tower.getFloors().size());
    }

    @Test
    public void lastFloorIsFinalBossRoomOnly() {
        Tower tower = new Tower();
        Floor lastFloor = tower.getFloors().get(tower.getFloors().size() - 1);
        assertEquals(1, lastFloor.getRooms().size());
        assertEquals(RoomType.BOSS, lastFloor.getRooms().get(0).getRoomType());
    }
}
