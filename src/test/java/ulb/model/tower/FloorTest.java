package ulb.model.tower;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloorTest {

    @Test
    public void checkRoomNumber() {
        Floor floor = new Floor(1, false);
        assertEquals(6, floor.getRooms().size());
    }
}
