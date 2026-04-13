package ulb.controller.towerManager;
import ulb.controller.BattleController;
import ulb.model.tower.Floor;
import  ulb.model.tower.Tower;
import ulb.model.Player;
import ulb.service.BugemonService;
import ulb.service.ItemService;

public class TowerManager {
	private Player player;
	private Tower tower;
	private int floorNumber;
	private FloorManager currentFloorManager;
	private final BugemonService bugemonService;
	private final ItemService itemService;

	public TowerManager(Player player, BugemonService bugemonService, ItemService itemService) {
		this.player = player;
		this.tower = new Tower();
		this.floorNumber = 0;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		this.currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player, this.getBugemonService(), this.getItemService());
	}

	public void nextFloor(){
		if (currentFloorManager.isFloorCompleted() && !isTowerCompleted()) {
			floorNumber++;
			currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player, this.getBugemonService(), this.getItemService());
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

	public BattleController getCurrentBattleController() { return getCurrentRoomManager().getRoomBattleController();}

	public BugemonService getBugemonService() { return this.bugemonService; }
	public ItemService getItemService() { return this.itemService; }
}
