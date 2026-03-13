package ulb.model.tower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Floor {

    private int id;
    private boolean victory = false;
    private List<Room> rooms;

    public Floor(int id, boolean isFinalBoss) {
        this.id = id;
        rooms = new ArrayList<>();
        if (isFinalBoss) { // only one room in the final boss floor
            Room room = new Room(1, RoomType.BOSS);
            rooms.add(room);
        } else {
            rooms = new ArrayList<>();
            buildFloor();
        }
    }


    private void buildFloor() {
        Room room1 = new Room(1, RoomType.BATTLE);
        Room room2 = new Room(2, RoomType.REWARD);
        Room room3 = new Room(3, RoomType.BATTLE);
        Room room4 = new Room(4, RoomType.BATTLE);
        Room room5 = new Room(5, RoomType.REWARD);
        Room room6 = new Room(6, RoomType.BOSS);
        List<Room> roomList = Arrays.asList(room1, room2, room3, room4, room5, room6);
        rooms.addAll(roomList);
    }

    public int getId() {return id;}

    public List<Room> getRooms() {return rooms;}

    public boolean isVictory() {return victory;}

    public void setVictory(boolean victory) {this.victory = victory;}
}
