package ulb.communication.old_types;

import ulb.communication.Message;
import ulb.communication.MessageType;
import ulb.controller.GameController;

public class TowerInfoMessage implements Message {

    private int floorNumber;
    private int roomNumber;

    public TowerInfoMessage(int floorNumber, int roomNumber){
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.TOWER_INFO;
    }

    @Override
    public Message handle(GameController controller) {
        // TODO Auto-generated method stub
        return null;
    }

    public int getFloorNumber(){return this.floorNumber;}
    public int getRoomNumber(){return this.roomNumber;}
}
