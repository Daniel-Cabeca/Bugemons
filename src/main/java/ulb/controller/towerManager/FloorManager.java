package ulb.controller.towerManager;

import ulb.model.Player;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;

public class FloorManager {
	private Player player;
	private Floor floor;
	private int currentRoomIndex;
	private RoomManager currentRoomManager;

	public FloorManager(Floor floor, Player player){
		this.player = player;
		this.floor = floor;
		this.currentRoomIndex = 0;
		this.currentRoomManager = new RoomManager(floor.getRooms().get(this.currentRoomIndex), floor.getId(), this.player);
	}

	public Floor getFloor() {return this.floor;}

	public RoomManager getCurrentRoomManager() {return this.currentRoomManager;}

	public void nextRoom(){
		if (currentRoomManager.isRoomCompleted() && !isFloorCompleted()) {
			currentRoomIndex++;
			currentRoomManager = new RoomManager(floor.getRooms().get(this.currentRoomIndex), floor.getId(), this.player);
		}
	}

	// Resets the current room so the player can retry it
	public void rewindRoom() {
		Room room = floor.getRooms().get(currentRoomIndex);
		room.setRoomCompleted(false);
		currentRoomManager = new RoomManager(room, floor.getId(), this.player);
	}

	public boolean isFloorCompleted() {
		for (Room room : this.floor.getRooms()){
			if (!room.isRoomCompleted()){
				floor.setFloorCompleted(false);
				return false;
			}
		}
		floor.setFloorCompleted(true);
		return true;
	}

	public Player getPlayer() {return player;}

	public void setPlayer(Player player) {this.player = player;}

	public int getCurrentRoomIndex() {return currentRoomIndex;}

}
