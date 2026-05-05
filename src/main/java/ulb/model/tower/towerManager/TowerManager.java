package ulb.model.tower.towerManager;
import ulb.model.tower.Floor;
import ulb.model.tower.RoomType;
import  ulb.model.tower.Tower;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.service.BugemonService;
import ulb.service.ItemService;
import ulb.service.TowerSaveService;

import java.util.List;

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
	private final TowerSaveService towerSaveService;

	/**
	 * Creates a tower manager and a new tower already saved depending on the setupNewTower boolean.
	 *
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 * @param setupNewTower tells if the tower saved or a new tower is used
	 */
	public TowerManager(Player player, boolean setupNewTower, BugemonService bugemonService, ItemService itemService, TowerSaveService towerSaveService) {
		this.player = player;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		this.towerSaveService = towerSaveService;
		setupTower(setupNewTower);
	}

	/**
	 * Creates a tower manager and create a new Tower.
	 * 
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 */
	public TowerManager(Player player, BugemonService bugemonService, ItemService itemService, TowerSaveService towerSaveService){
		this(player, true, bugemonService, itemService, towerSaveService);
	}

	private void setupTower(boolean setupNewTower){
		Tower tower;
		if (setupNewTower){
			tower = new Tower();
		} else {
			tower = towerSaveService.getTowerSave(player);
		}
		this.tower = tower;
		this.floorNumber = this.tower.getCurrentFloorId() - 1;
		this.currentFloorManager = new FloorManager(this.tower.getCurrentFloor(), this.player, this.getBugemonService(), this.getItemService());
		
		saveTowerInfo();
	}

	/** Advances to the specified room and updates floor when needed. 
	 * 
	 * @param targetRoomId ID of the room to move to
	 * @return True if move was successful, false otherwise
	*/
	public boolean moveToRoom(int targetRoomId){
		if(!this.currentFloorManager.moveToRoom(targetRoomId)){
			return false;
		}
		nextFloor();
		return true;
	}

	/** Advances to next floor if current floor is completed. */
	public void nextFloor(){
		if (currentFloorManager.isFloorCompleted() && !isTowerCompleted()) {
			floorNumber++;
			currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player, this.getBugemonService(), this.getItemService());
		}
		saveTowerInfo();
	}

	public void saveTowerInfo(){
		this.towerSaveService.saveTowerInfo(this.tower, this.player);
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

	public boolean isFinalFloor() {
		return floorNumber == 8;
	}

	/**
	 * Sets current managed room completion status and save the tower.
	 * @param status the status of the current room
	 */
	public void setCurrentRoomCompleted(boolean status){
		this.getCurrentRoomManager().setRoomCompleted(status);
		saveTowerInfo();
	}

    /**
     * @return the list of the ids of the current floor's cleared rooms
     */
	public List<Integer> getCurrentFloorClearedRooms() {
		return this.tower.getCurrentFloorCompletedRoomsId();
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
	public int getCurrentRoomId() {
		return getCurrentFloorManager().getCurrentRoomId();
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
