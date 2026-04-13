package ulb.model.tower.towerManager;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.bugemon.Bugemon;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.service.strategy.AI;
import ulb.service.strategy.StrategyRandom;
import ulb.service.BugemonService;
import ulb.service.ItemService;


/**
 * Manages one tower room lifecycle and its generated content.
 */
public class RoomManager {
	private Room room;
	private final int floorNumber;
	private Player player;
	private Battle battle;
	private boolean isTeamA;
	private final BugemonService bugemonService;
	private final ItemService itemService;

	/**
	 * Creates a manager for a specific room.
	 *
	 * @param room Managed room
	 * @param floorNumber Parent floor number
	 * @param player Current player
	 * @param bugemonService Bugemon service
	 * @param itemService Item service
	 */
	public RoomManager(Room room, int floorNumber, Player player, BugemonService bugemonService, ItemService itemService){
		this.room = room;
		this.floorNumber = floorNumber;
		this.player = player;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		initializeRoomContent(room.getRoomType());
	}

	/** Indicates whether managed room is completed. */
	public boolean isRoomCompleted() {
		return room.isRoomCompleted();
	}

	/** Sets managed room completion status. */
	public void setRoomCompleted(boolean status) {room.setRoomCompleted(status);}

	/** Returns item service. */
	public ItemService getItemService() { return this.itemService; }
	/** Returns bugemon service. */
	public BugemonService getBugemonService() { return this.bugemonService; }

	/**
	 * Initializes room content according to room type.
	 *
	 * @param type Room type
	 */
	public void initializeRoomContent(RoomType type) {
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
	public void initializeBattleRoom(boolean isBossBattle){
		Team playerTeam = player.getTeam();
		Team opponentTeam = new Team();

		try{
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam, this.getBugemonService());
		}catch(Exception e){
			System.err.println(e);
		}

		this.battle = new Battle(playerTeam, opponentTeam, player, new Player(this.getItemService()));
		battle.setFloorNumber(floorNumber);
		
		if (isBossBattle) {
			battle.enableBossBattle();
		}

		Thread botPlayer = new AI(battle, new StrategyRandom());
		botPlayer.setDaemon(true);
		botPlayer.start();
	}

	/** Returns managed room. */
	public Room getRoom() {return room;}

	/** Sets managed room. */
	public void setRoom(Room room) {this.room = room;}

	/** Returns current player. */
	public Player getPlayer() {return player;}

	/** Sets current player. */
	public void setPlayer(Player player) {this.player = player;}

	/** Returns current battle instance. */
	public Battle getBattle() {return this.battle;}

	/** Sets current battle instance. */
	public void setBattle(Battle battle) {this.battle = battle;}
}
