package ulb.model.tower.towerManager;

import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.item.Inventory;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.service.BugemonService;
import ulb.service.ItemService;
import ulb.service.strategy.AI;
import ulb.service.strategy.StrategyRandom;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Manages one tower room lifecycle and its generated content.
 */
public class RoomManager {
	private final Logger LOGGER = Logger.getLogger(RoomManager.class.getName());
	private final int floorNumber;
	private final BugemonService bugemonService;
	private final ItemService itemService;
	private final Room room;
	private final Player player;
	private Battle battle;

	/**
	 * Creates a manager for a specific room.
	 *
	 * @param room Managed room
	 * @param floorNumber Parent floor number
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 */
	public RoomManager(Room room, int floorNumber, Player player, BugemonService bugemonService,
					   ItemService itemService) {
		this.room = room;
		this.floorNumber = floorNumber;
		this.player = player;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		initializeRoomContent(room.getRoomType());
	}

	/**
	 * Initializes room content according to room type.
	 *
	 * @param type Room type
	 */
	public void initializeRoomContent(RoomType type) {
		if (this.room.isRoomCompleted()) {
			return;
		}

		switch (type) {
			case BATTLE:
				initializeBattleRoom(false);
				break;

			case BOSS:
				initializeBattleRoom(true);
				break;

			default:
				break;
		}
	}

	/**
	 * Initializes a battle room, optionally as boss encounter.
	 *
	 * @param isBossBattle True for boss battle settings
	 */
	public void initializeBattleRoom(boolean isBossBattle) {
		Team playerTeam = player.getTeam();
		Team opponentTeam = new Team();
		try {
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam, this.getBugemonService());
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to generate random opponent team, defaulting to empty team.");
		}
		Inventory playerBInventory = this.getItemService().createStarterInventory();
		this.battle = new Battle(playerTeam, opponentTeam, player, new Player("PlayerB", -1, playerBInventory));
		battle.setFloorNumber(floorNumber);

		if (isBossBattle) {
			battle.enableBossBattle();
		}
		Thread botPlayer = new AI(battle, new StrategyRandom());
		botPlayer.setDaemon(true);
		botPlayer.start();
	}

	/** Returns bugemon service. */
	public BugemonService getBugemonService() { return this.bugemonService; }

	/** Returns item service. */
	public ItemService getItemService() { return this.itemService; }

	/** Indicates whether managed room is completed. */
	public boolean isRoomCompleted() {
		return room.isRoomCompleted();
	}

	/** Sets managed room completion status. */
	public void setRoomCompleted(boolean status) { room.setRoomCompleted(status); }

	/** Returns managed room. */
	public Room getRoom() { return room; }

	/** Returns current battle instance. */
	public Battle getBattle() { return this.battle; }
}
