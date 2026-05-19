package ulb.model.tower.towerManager;

import ulb.exceptions.EntityNotFoundException;
import ulb.model.Player;
import ulb.model.tower.Floor;
import ulb.model.tower.Room;
import ulb.service.BugemonService;
import ulb.service.ItemService;

/**
 * Manages progression and state for a single tower floor.
 */
public class FloorManager {
	private final BugemonService bugemonService;
	private final ItemService itemService;
	private Player player;
	private Floor floor;
	private int currentRoomId;
	private int previousRoomId;
	private RoomManager currentRoomManager;

	/**
	 * Creates a floor manager.
	 *
	 * @param floor Managed floor
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 */
	public FloorManager(Floor floor, Player player, BugemonService bugemonService, ItemService itemService) throws EntityNotFoundException {
		this.player = player;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		this.floor = floor;

		this.currentRoomId = floor.getStartRoomId();
		this.previousRoomId = this.currentRoomId;
		this.currentRoomManager = new RoomManager(floor.getRoomById(currentRoomId), floor.getId(), this.player,
				this.getBugemonService(), this.getItemService());
	}

	/** Returns bugemon service. */
	public BugemonService getBugemonService() { return this.bugemonService; }

	/** Returns item service. */
	public ItemService getItemService() { return this.itemService; }

	/** Returns managed floor. */
	public Floor getFloor() { return this.floor; }

	/**
	 * Advances to the specified room if it's adjacent and the current one is completed.
	 *
	 * @param targetRoomId ID of the room to move to
	 */
	public void moveToRoom(int targetRoomId) throws EntityNotFoundException {
		int currentRoomId = getCurrentRoomId();

		if (targetRoomId == currentRoomId) return;
		if (!currentRoomManager.isRoomCompleted()) return;
		if (!floor.getAdjacentRoomsIds(currentRoomId).contains(targetRoomId)) return;

		Room target = floor.getRoomById(targetRoomId);

		this.previousRoomId = currentRoomId;
		this.currentRoomId = target.getId();
		currentRoomManager = new RoomManager(target, floor.getId(), player, getBugemonService(), getItemService());
	}

	/** Returns current room id. */
	public int getCurrentRoomId() { return getRoom().getId(); }

	/** Returns current room. */
	public Room getRoom() { return this.getCurrentRoomManager().getRoom(); }

	/** Returns current room manager. */
	public RoomManager getCurrentRoomManager() { return this.currentRoomManager; }

	/** Resets current room to the previous one so the player can retry it. */
	public void rewindRoom() throws EntityNotFoundException {
		Room fledRoom = floor.getRoomById(this.currentRoomId);
		fledRoom.setRoomCompleted(false);

		this.currentRoomId = this.previousRoomId;
		Room previousRoom = floor.getRoomById(this.currentRoomId);
		currentRoomManager = new RoomManager(previousRoom, floor.getId(), this.player, this.getBugemonService(),
				this.getItemService());
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

}
