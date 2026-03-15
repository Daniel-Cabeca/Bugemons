package ulb.controller.towerManager;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import  ulb.model.tower.Tower;
import ulb.model.Player;

public class TowerManager {
	private Player player;
	private Tower tower;
	private int floorNumber;
	private FloorManager currentFloorManager;

	public TowerManager(Player player) {
		this.player = player;
		this.tower = new Tower();
		this.floorNumber = 0;
		this.currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player);
	}

	public void nextFloor(){
		if (currentFloorManager.isFloorCompleted() && !isTowerCompleted()) {
			floorNumber++;
			currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player);
		}
	}

	public boolean isTowerCompleted(){
		for (Floor floor : this.tower.getFloors()){
			if (!floor.isFloorCompleted()){
				tower.setTowerCompleted(false);
				return false;
			}
		}
		tower.setTowerCompleted(true);
		return true;
	}

	public Player getPlayer() {return player;}

	public void setPlayer(Player player) {this.player = player;}

	public Tower getTower() {return tower;}

	public void setTower(Tower tower) {this.tower = tower;}

	public int getFloorNumber() {return floorNumber;}

	public void setFloorNumber(int floorNumber) {this.floorNumber = floorNumber;}

	public FloorManager getCurrentFloorManager() {return currentFloorManager;}

	public void setCurrentFloorManager(FloorManager currentFloorManager) {this.currentFloorManager = currentFloorManager;}

	public int getCurrentRoomIndex() {return getCurrentFloorManager().getCurrentRoomIndex();}

	public RoomManager getCurrentRoomManager() {return this.currentFloorManager.getCurrentRoomManager();}
}
