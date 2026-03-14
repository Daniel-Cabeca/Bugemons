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

	public TowerManager() {
		this.tower = new Tower();
		this.floorNumber = 0;
		this.currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber));
	}

	public void nextFloor(){
		if (currentFloorManager.isFloorCompleted() && !isTowerCompleted()) {
			floorNumber++;
			currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber));
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



}
