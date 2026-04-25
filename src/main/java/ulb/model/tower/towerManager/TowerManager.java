package ulb.model.tower.towerManager;
import ulb.model.tower.Floor;
import ulb.model.tower.RoomType;
import  ulb.model.tower.Tower;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.service.BugemonService;
import ulb.service.ItemService;

/**
 * Manages overall tower progression across floors and rooms.
 */
public class TowerManager {
	private Player player;
	private Tower tower;
	private int floorNumber;
	private FloorManager currentFloorManager;
	private final BugemonService bugemonService;
	private final ItemService itemService;

	/**
	 * Creates a tower manager.
	 *
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 */
	public TowerManager(Player player, BugemonService bugemonService, ItemService itemService) {
		this.player = player;
		this.tower = new Tower();
		this.floorNumber = 0;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		this.currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player, this.getBugemonService(), this.getItemService());
	}

	/** Advances to next room and updates floor when needed. */
	public void nextRoom(){
		this.currentFloorManager.nextRoom();
		nextFloor();
	}

	/** Advances to next floor if current floor is completed. */
	public void nextFloor(){
		if (currentFloorManager.isFloorCompleted() && !isTowerCompleted()) {
			floorNumber++;
			currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player, this.getBugemonService(), this.getItemService());
		}
	}

	/**
	 * Checks whether tower is fully completed.
	 *
	 * @return True if all floors are completed
	 */
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

	/** Returns whether current room is completed. */
	public boolean isRoomCompleted(){
		return this.getCurrentRoomManager().isRoomCompleted();
	}

	/** Returns current player. */
	public Player getPlayer() {return player;}

	/** Sets current player. */
	public void setPlayer(Player player) {this.player = player;}

	/** Returns tower definition. */
	public Tower getTower() {return tower;}

	/** Sets tower definition. */
	public void setTower(Tower tower) {this.tower = tower;}

	/** Returns current floor number as displayed in game. */
	public int getFloorNumber() {
		// + 2 since the floors start at NO2 and not 0
		return floorNumber+2;
	}

	/** Returns current floor manager. */
	public FloorManager getCurrentFloorManager() {return currentFloorManager;}

	/** Returns current room index as displayed in game. */
	public int getCurrentRoomIndex() {
		// + 1 so the rooms start at 1 and not 0
		return getCurrentFloorManager().getCurrentRoomIndex()+1;
	}

	/** Returns current room manager. */
	public RoomManager getCurrentRoomManager() {return this.currentFloorManager.getCurrentRoomManager();}

	/** Returns current battle. */
	public Battle getCurrentBattle() { return getCurrentRoomManager().getBattle();}

	/** Returns current room type. */
	public RoomType getCurrentRoomType() {return this.currentFloorManager.getRoom().getRoomType();}

	/** Returns bugemon service. */
	public BugemonService getBugemonService() { return this.bugemonService; }
	/** Returns item service. */
	public ItemService getItemService() { return this.itemService; }
}
