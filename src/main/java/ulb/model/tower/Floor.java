package ulb.model.tower;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Floor {

    private int id;
    private boolean completed = false;
    private List<Room> rooms;

    public Floor(int id, boolean isFinalBoss) {
        this.id = id;
        this.rooms = new ArrayList<>();

        if (isFinalBoss) {
            Room room = new Room(1, RoomType.BOSS);
            rooms.add(room);
        } else {
            rooms = new ArrayList<>();
            buildFloor();
        }
    }

    /**
     * Builds the floor with the correct room types
     */
    private void buildFloor() {
        Room room1 = new Room(1, RoomType.REWARD);
        Room room2 = new Room(2, RoomType.BATTLE);
        Room room3 = new Room(3, RoomType.BATTLE);
        Room room4 = new Room(4, RoomType.EMPTY);
        Room room5 = new Room(5, RoomType.BATTLE);
        Room room6 = new Room(6, RoomType.REWARD);
		Room room7 = new Room(7, RoomType.BOSS);
        List<Room> roomList = Arrays.asList(room1, room2, room3, room4, room5, room6, room7);
        rooms.addAll(roomList);
    }

    public int getId() {return id;}

    public List<Room> getRooms() {return rooms;}

	public Room getRoomById(int roomId) {
		for (Room room : rooms) {
			if (room.getId() == roomId) {
				return room;
			}
		}
		return null;
	}

	public List<Integer> getAdjacentRoomsIds(int roomId) {
		return switch (roomId) {
			case 1 -> List.of(2);
			case 2 -> List.of(1, 3);
			case 3 -> List.of(2, 4);
			case 4 -> List.of(3, 5, 7);
			case 5 -> List.of(4, 6);
			case 6 -> List.of(5);
			case 7 -> List.of(4);
			default -> new ArrayList<>();
		};
	}

    public boolean isFloorCompleted() {return completed;}

    public void setFloorCompleted(boolean status) {this.completed = status;}
}
