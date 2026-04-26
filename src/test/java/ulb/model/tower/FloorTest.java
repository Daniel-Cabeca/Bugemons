package ulb.model.tower;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FloorTest {

    @Test
    public void amountOfRoomsInFloorOnInitialisation() {
        Floor floor = new Floor(1, false);
        assertEquals(7, floor.getRooms().size());
    }
}
