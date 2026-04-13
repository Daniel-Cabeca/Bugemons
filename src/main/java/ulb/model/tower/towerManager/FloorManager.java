package ulb.model.tower.towerManager;

import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import ulb.service.BugemonService;
import ulb.service.ItemService;

/**
 * Manages progression and state for a single tower floor.
 */
public class FloorManager {
	private Player player;
	private Floor floor;
	private int currentRoomIndex;
	private RoomManager currentRoomManager;
	private final BugemonService bugemonService;
	private final ItemService itemService;

	/**
	 * Creates a floor manager.
	 *
	 * @param floor Managed floor
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 */
	public FloorManager(Floor floor, Player player, BugemonService bugemonService, ItemService itemService){
		this.player = player;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		this.floor = floor;
		this.currentRoomIndex = 0;
		this.currentRoomManager = new RoomManager(floor.getRooms().get(this.currentRoomIndex), floor.getId(), this.player, this.getBugemonService(), this.getItemService());
	}

	/** Returns managed floor. */
	public Floor getFloor() {return this.floor;}

	/** Returns current room manager. */
	public RoomManager getCurrentRoomManager() {return this.currentRoomManager;}

	/** Returns bugemon service. */
	public BugemonService getBugemonService() { return this.bugemonService; }
	/** Returns item service. */
	public ItemService getItemService() { return this.itemService; }

	/** Advances to next room if current one is completed. */
	public void nextRoom(){
		if (currentRoomManager.isRoomCompleted() && !isFloorCompleted()) {
			currentRoomIndex++;
			currentRoomManager = new RoomManager(floor.getRooms().get(this.currentRoomIndex), floor.getId(), this.player, this.getBugemonService(), this.getItemService());
		}
	}

	/** Resets current room so the player can retry it. */
	public void rewindRoom() {
		Room room = floor.getRooms().get(currentRoomIndex);
		room.setRoomCompleted(false);
		currentRoomManager = new RoomManager(room, floor.getId(), this.player, this.getBugemonService(), this.getItemService());
	}

	/**
	 * Checks whether every room in this floor is completed.
	 *
	 * @return True if floor is completed
	 */
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

	/** Returns current player. */
	public Player getPlayer() {return player;}

	/** Sets current player. */
	public void setPlayer(Player player) {this.player = player;}

	/** Returns current room index. */
	public int getCurrentRoomIndex() {return currentRoomIndex;}

	/** Returns current battle instance. */
	public Battle getCurrentBattle() {return getCurrentRoomManager().getBattle();}

	/** Returns current room. */
	public Room getRoom(){return this.getCurrentRoomManager().getRoom();}

}
