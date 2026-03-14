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

	public void setRoomCompleted(boolean status) {this.setRoomCompleted(status);}

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

	}


	public void initializeBosslBattleRoom(){

	}

	public void initializeRewardRoom(){

	}

}
