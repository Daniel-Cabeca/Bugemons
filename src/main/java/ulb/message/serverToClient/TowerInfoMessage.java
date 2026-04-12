package ulb.message.serverToClient;

import java.io.Serializable;

public class TowerInfoMessage implements Serializable {

    private int floorNumber;
    private int roomNumber;

    public TowerInfoMessage(int floorNumber, int roomNumber){
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
	}

    public int getFloorNumber(){return this.floorNumber;}
    public int getRoomNumber(){return this.roomNumber;}
}
