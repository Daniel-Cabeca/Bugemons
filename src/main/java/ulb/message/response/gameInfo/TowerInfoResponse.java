package ulb.message.response.gameInfo;

import ulb.message.response.Response;

import java.util.List;

public class TowerInfoResponse extends Response {
    private final int floorNumber;
    private final int roomNumber;
    private final List<Integer> clearedRooms;

    public TowerInfoResponse(int floorNumber, int roomNumber, List<Integer> clearedRooms) {
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
        this.clearedRooms = clearedRooms;
	}

    public int getFloorNumber(){return this.floorNumber;}
    public int getRoomNumber(){return this.roomNumber;}

    public List<Integer> getClearedRooms() { return this.clearedRooms;}

}
