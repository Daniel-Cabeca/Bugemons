package ulb.model.tower.towerManager;

import java.util.Optional;

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
	private int currentRoomId;
	private int previousRoomId;
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

		this.currentRoomId = floor.getStartRoomId();
		this.previousRoomId = this.currentRoomId;
		if (floor.getRoomById(currentRoomId).isPresent()){
			this.currentRoomManager = new RoomManager(floor.getRoomById(currentRoomId).get(), floor.getId(), this.player, this.getBugemonService(), this.getItemService());
		} else {
			// TODO Error
		}
		
	}

	/** Returns managed floor. */
	public Floor getFloor() {return this.floor;}

	/** Returns current room manager. */
	public RoomManager getCurrentRoomManager() {return this.currentRoomManager;}

	/** Returns bugemon service. */
	public BugemonService getBugemonService() { return this.bugemonService; }
	/** Returns item service. */
	public ItemService getItemService() { return this.itemService; }

	/** Advances to the specified room if it's adjacent and the current one is completed.
	 * @param targetRoomId ID of the room to move to
	 */
	public boolean moveToRoom(int targetRoomId){
		int currentRoomId = getCurrentRoomId();

        if (targetRoomId == currentRoomId) return true;
        if (!currentRoomManager.isRoomCompleted()) return false;
        if (!floor.getAdjacentRoomsIds(currentRoomId).contains(targetRoomId)) return false;

        Optional<Room> target = floor.getRoomById(targetRoomId);
        if (target.isEmpty()) return false;

		this.previousRoomId = currentRoomId;
        this.currentRoomId = target.get().getId();
        currentRoomManager = new RoomManager(target.get(), floor.getId(), player, getBugemonService(), getItemService());
        return true;
	}

	/** Resets current room to the previous one so the player can retry it. */
	public void rewindRoom() {
		Optional<Room> fledRoom = floor.getRoomById(this.currentRoomId);
		if (fledRoom.isPresent()){
			fledRoom.get().setRoomCompleted(false);
		}

		this.currentRoomId = this.previousRoomId;
		Optional<Room> previousRoom =  floor.getRoomById(this.currentRoomId);
		if (previousRoom.isPresent()){
			currentRoomManager = new RoomManager(previousRoom.get(), floor.getId(), this.player, this.getBugemonService(), this.getItemService());
		}
	}

	/**
	 * Checks whether the floor is completed based on if the boss battle is won.
	 *
	 * @return True if the boss battle is won, false otherwise
	 */
	public boolean isFloorCompleted() {
		if (!floor.isBossCompleted()) {
			floor.setFloorCompleted(false);
			return false;
		}
		floor.setFloorCompleted(true);
		return true;
	}

	/** Returns current room. */
	public Room getRoom() { return this.getCurrentRoomManager().getRoom(); }

	/** Returns current room id. */
	public int getCurrentRoomId() { return getRoom().getId(); }

}
