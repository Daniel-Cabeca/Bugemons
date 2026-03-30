package ulb.communication.types;

import ulb.communication.Message;

public class RewardPlaceMessage implements Message {
    private int floorNumber;
    private int roomNumber;

    public RewardPlaceMessage(int floorNumber, int roomNumber) {
        this.floorNumber = floorNumber;
        this.roomNumber = roomNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.REWARD_PLACE;
    }
}
