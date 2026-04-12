package ulb.model.tower.towerManager;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import  ulb.model.tower.Tower;
import ulb.model.Player;
import ulb.model.battle.Battle;

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

	public int getFloorNumber() {
		// + 2 since the floors start at NO2 and not 0
		return floorNumber+2;
	}

	public FloorManager getCurrentFloorManager() {return currentFloorManager;}

	public int getCurrentRoomIndex() {
		// + 1 so the rooms start at 1 and not 0
		return getCurrentFloorManager().getCurrentRoomIndex()+1;
	}

	public RoomManager getCurrentRoomManager() {return this.currentFloorManager.getCurrentRoomManager();}

	public Battle getCurrentBattle() { return getCurrentRoomManager().getBattle();}

	public RoomType getCurrentRoomType() {return this.currentFloorManager.getRoom().getRoomType();}
}
