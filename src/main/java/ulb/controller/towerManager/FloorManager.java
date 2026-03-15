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
		this.currentRoomManager = new RoomManager(floor.getRooms().get(this.currentRoomIndex),this.player);
	}

	public Floor getFloor() {return this.floor;}

	public void setFloor(Floor newFloor) {this.floor = newFloor;}

	public RoomManager getCurrentRoomManager() {return this.currentRoomManager;}

	public void setCurrentRoomManager(RoomManager newRoomManager) {this.currentRoomManager = newRoomManager;}

	public void nextRoom(){
		if (currentRoomManager.isRoomCompleted() && !isFloorCompleted()) {
			currentRoomIndex++;
			currentRoomManager = new RoomManager(floor.getRooms().get(this.currentRoomIndex), this.player);
		}
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

	public void setCurrentRoomIndex(int currentRoomIndex) {this.currentRoomIndex = currentRoomIndex;}




}
