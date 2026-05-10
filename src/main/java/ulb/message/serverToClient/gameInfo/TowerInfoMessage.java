package ulb.message.serverToClient.gameInfo;

import java.io.Serializable;
import java.util.List;

public class TowerInfoMessage implements Serializable {
    private final int floorNumber;
    private final int roomNumber;
    private final List<Integer> clearedRooms;

    public TowerInfoMessage(int floorNumber, int roomNumber, List<Integer> clearedRooms) {
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
        this.clearedRooms = clearedRooms;
	}

    public int getFloorNumber(){return this.floorNumber;}
    public int getRoomNumber(){return this.roomNumber;}

    public List<Integer> getClearedRooms() { return this.clearedRooms;}

}
