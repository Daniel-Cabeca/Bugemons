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


public class RoomManager {
	private Room room;
	private final int floorNumber;
	private Player player;
	private Battle battle;
	private boolean isTeamA;

	public RoomManager(Room room, int floorNumber, Player player){
		this.room = room;
		this.floorNumber = floorNumber;
		this.player = player;
		initializeRoomContent(room.getRoomType());
	}

	public boolean isRoomCompleted() {
		if (room.getRoomType() != RoomType.REWARD){
			room.setRoomCompleted(this.battle.isGameFinished());
			
			if (room.isRoomCompleted()){
				this.battle.resetFightStats();
			}
			return this.battle.isGameFinished();
		}
		return room.isRoomCompleted();
	}

	public void setRoomCompleted(boolean status) {room.setRoomCompleted(status);}

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
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);
		}catch(Exception e){
			System.err.println(e);
		}

		this.battle = new Battle(playerTeam, opponentTeam, player);
		battle.setFloorNumber(floorNumber);
		
		if (isBossBattle) {
			battle.enableBossBattle();
		}

		Thread botPlayer = new AI(battle, new StrategyRandom());
		botPlayer.setDaemon(true);
		botPlayer.start();
	}

	public Room getRoom() {return room;}

	public void setRoom(Room room) {this.room = room;}

	public Player getPlayer() {return player;}

	public void setPlayer(Player player) {this.player = player;}

	public Battle getBattle() {return this.battle;}

	public void setBattle(Battle battle) {this.battle = battle;}
}
