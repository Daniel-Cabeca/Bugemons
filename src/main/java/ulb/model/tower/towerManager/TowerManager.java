package ulb.model.tower.towerManager;

import ulb.exceptions.EntityNotFoundException;
import ulb.exceptions.LoadException;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.tower.Floor;
import ulb.model.tower.RoomType;
import ulb.model.tower.Tower;
import ulb.service.BugemonService;
import ulb.service.ItemService;
import ulb.service.TeamService;
import ulb.service.TowerSaveService;

import java.util.List;

/**
 * Manages overall tower progression across floors and rooms.
 */
public class TowerManager {
	private final BugemonService bugemonService;
	private final ItemService itemService;
	private final TeamService teamService;
	private final TowerSaveService towerSaveService;
	private final Player player;
	private Tower tower;
	private int floorNumber;
	private boolean fledBattle;
	private FloorManager currentFloorManager;

	/**
	 * Creates a tower manager and create a new Tower.
	 *
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 */
	public TowerManager(Player player, BugemonService bugemonService, ItemService itemService, TeamService teamService
			, TowerSaveService towerSaveService) throws LoadException, EntityNotFoundException {
		this(player, true, bugemonService, itemService, teamService, towerSaveService);
	}

	/**
	 * Creates a tower manager and a new tower already saved depending on the setupNewTower boolean.
	 *
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 * @param setupNewTower tells if the tower saved or a new tower is used
	 */
	public TowerManager(Player player, boolean setupNewTower, BugemonService bugemonService, ItemService itemService,
						TeamService teamService, TowerSaveService towerSaveService) throws LoadException,
			EntityNotFoundException {
		this.player = player;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		this.teamService = teamService;
		this.towerSaveService = towerSaveService;
		setupTower(setupNewTower);
	}

	private void setupTower(boolean setupNewTower) throws LoadException, EntityNotFoundException {
		Tower tower;
		if (setupNewTower) {
			tower = new Tower();
			towerSaveService.deleteTowerInfo(player);
			teamService.deleteTowerTeam(player);
		} else {
			tower = towerSaveService.getTowerSave(player);
		}
		this.tower = tower;
		this.floorNumber = this.tower.getCurrentFloorId() - 1;
		this.currentFloorManager = new FloorManager(this.tower.getCurrentFloor(), this.player,
				this.getBugemonService(), this.getItemService());

		saveTowerInfo();
	}

	/** Returns bugemon service. */
	public BugemonService getBugemonService() { return this.bugemonService; }

	/** Returns item service. */
	public ItemService getItemService() { return this.itemService; }

	public void saveTowerInfo() throws LoadException, EntityNotFoundException {
		this.teamService.insertTowerTeam(this.player);
		this.towerSaveService.saveTowerInfo(this.tower, this.player);
	}

	/**
	 * Advances to the specified room and updates floor when needed.
	 *
	 * @param targetRoomId ID of the room to move to
	 * @return True if move was successful, false otherwise
	 */
	public void moveToRoom(int targetRoomId) throws LoadException, EntityNotFoundException {
		this.currentFloorManager.moveToRoom(targetRoomId);

		if (this.getCurrentRoomId() == targetRoomId) {
			nextFloor();
		}
	}

	/** Returns current room index as displayed in game. */
	public int getCurrentRoomId() {
		return getCurrentFloorManager().getCurrentRoomId();
	}

	/** Advances to next floor if current floor is completed. */
	public void nextFloor() throws LoadException, EntityNotFoundException {
		if (currentFloorManager.isFloorCompleted() && !isTowerCompleted()) {
			floorNumber++;
			currentFloorManager = new FloorManager(tower.getFloors().get(floorNumber), this.player,
					this.getBugemonService(), this.getItemService());
		}
		saveTowerInfo();
	}

	/** Returns current floor manager. */
	public FloorManager getCurrentFloorManager() { return currentFloorManager; }

	/**
	 * Checks whether tower is fully completed.
	 *
	 * @return True if all floors are completed
	 */
	public boolean isTowerCompleted() {
		for (Floor floor : this.tower.getFloors()) {
			if (!floor.isFloorCompleted()) {
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
	 *
	 * @param status the status of the current room
	 */
	public void setCurrentRoomCompleted(boolean status) throws LoadException, EntityNotFoundException {
		this.getCurrentRoomManager().setRoomCompleted(status);
		saveTowerInfo();
	}

	/** Returns current room manager. */
	public RoomManager getCurrentRoomManager() { return this.currentFloorManager.getCurrentRoomManager(); }

	/**
	 * @return the list of the ids of the current floor's cleared rooms
	 */
	public List<Integer> getCurrentFloorClearedRooms() throws EntityNotFoundException {
		return this.tower.getCurrentFloorCompletedRoomsId();
	}

	/** Returns tower definition. */
	public Tower getTower() { return tower; }

	/** Returns current floor number as displayed in game. */
	public int getFloorNumber() {
		// + 2 since the floors start at NO2 and not 0
		return floorNumber + 2;
	}

	/** Returns current battle. */
	public Battle getCurrentBattle() { return getCurrentRoomManager().getBattle(); }

	/** Returns current room type. */
	public RoomType getCurrentRoomType() { return this.currentFloorManager.getRoom().getRoomType(); }

	public void setFledBattle(boolean fledBattle) { this.fledBattle = fledBattle; }

	public boolean hasFledBattle() { return this.fledBattle; }
}
