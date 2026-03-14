package ulb.controller.towerManager;
import ulb.controller.BattleController;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;


public class RoomManager {
	private Room room;
	private Player player;
	private Battle battle;
	private boolean isTeamA;

	public RoomManager(Room room){
		this.room = room;
		initializeRoomContent(room.getRoomType());
	}

	public boolean isRoomCompleted() {return room.isRoomCompleted();}

	public void setRoomCompleted(boolean status) {room.setRoomCompleted(status);}

	public void initializeRoomContent(RoomType type) {
		switch (type) {
			case BATTLE:
				initializeNormalBattleRoom();
				break;

			case BOSS:
				initializeBosslBattleRoom();
				break;

			case REWARD:
				initializeRewardRoom();
				break;

			default:
				break;
		}
	}

	public void initializeNormalBattleRoom(){
		// TODO
	}


	public void initializeBosslBattleRoom(){
		// TODO
	}

	public void initializeRewardRoom(){
		// TODO
	}

	public Room getRoom() {return room;}

	public void setRoom(Room room) {this.room = room;}

	public Player getPlayer() {return player;}

	public void setPlayer(Player player) {this.player = player;}

	public Battle getBattle() {return battle;}

	public void setBattle(Battle battle) {this.battle = battle;}

	public boolean isTeamA() {return isTeamA;}

	public void setTeamA(boolean teamA) {isTeamA = teamA;}

}
