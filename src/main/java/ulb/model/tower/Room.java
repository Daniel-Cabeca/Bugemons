package ulb.model.tower;

public class Room {

    private int id;
    private RoomType roomType;

    public Room(int id, RoomType roomType) {
        this.id = id;
        this.roomType = roomType;
    }

    public RoomType getRoomType() {return roomType;}

    public int getId() {return id;}
}
