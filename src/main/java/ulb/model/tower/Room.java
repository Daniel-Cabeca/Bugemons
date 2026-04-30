package ulb.model.tower;

/**
 * Represents a room in a tower floor.
 */
public class Room {

    private int id;
    private RoomType roomType;
	private boolean completedRoom = false;

	/**
	 * Creates a room definition.
	 *
	 * @param id Room id
	 * @param roomType Room type
	 */
    public Room(int id, RoomType roomType) {
        this.id = id;
        this.roomType = roomType;
		if (roomType == RoomType.EMPTY) {
			this.completedRoom = true;
		}
    }


    public RoomType getRoomType() {return roomType;}


    public int getId() {return id;}


	public boolean isRoomCompleted() {return this.completedRoom;}

	/**
	 * Sets completion state of this room.
	 *
	 * @param status Completion status
	 */
	public void setRoomCompleted(boolean status) {this.completedRoom = status;}
}
