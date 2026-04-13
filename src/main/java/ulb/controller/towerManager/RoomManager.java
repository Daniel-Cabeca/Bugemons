package ulb.controller.towerManager;
import ulb.controller.BattleController;
import ulb.controller.strategy.StrategyRandom;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.team.OpponentTeamGenerator;
import ulb.model.team.Team;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.service.BugemonService;
import ulb.service.ItemService;


public class RoomManager {
	private Room room;
	private final int floorNumber;
	private Player player;
	private Battle battle;
	private boolean isTeamA;
	private BattleController roomBattleController;
	private final BugemonService bugemonService;
	private final ItemService itemService;

	public RoomManager(Room room, int floorNumber, Player player, BugemonService bugemonService, ItemService itemService){
		this.room = room;
		this.floorNumber = floorNumber;
		this.player = player;
		this.bugemonService = bugemonService;
		this.itemService = itemService;
		initializeRoomContent(room.getRoomType());
	}

	public boolean isRoomCompleted() {return room.isRoomCompleted();}

	public void setRoomCompleted(boolean status) {room.setRoomCompleted(status);}

	public ItemService getItemService() { return this.itemService; }
	public BugemonService getBugemonService() { return this.bugemonService; }

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

	public void initializeBattleRoom(boolean isBossBattle){
		Team playerTeam = player.getTeam();
		Team opponentTeam = new Team();
		try{
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam, this.getBugemonService());
		}catch(Exception e){
			System.err.println(e);
		}
		Battle battle = new Battle(playerTeam, opponentTeam, player, new Player(this.getItemService()));
		battle.setFloorNumber(floorNumber);
		if (isBossBattle) {
			battle.enableBossBattle();
		}
		this.roomBattleController = new BattleController(player, battle, ParticipantLabel.TEAM_A, this.getItemService());
		StrategyRandom strategyRandom = new StrategyRandom(battle, this.getItemService());
		Thread thread = new Thread(strategyRandom);
		thread.setDaemon(true);
		thread.start();
	}

	public Room getRoom() {return room;}

	public void setRoom(Room room) {this.room = room;}

	public Player getPlayer() {return player;}

	public void setPlayer(Player player) {this.player = player;}

	public Battle getBattle() {return battle;}

	public void setBattle(Battle battle) {this.battle = battle;}

	public BattleController getRoomBattleController() {return this.roomBattleController;}

}
