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


public class RoomManager {
	private Room room;
	private final int floorNumber;
	private Player player;
	private Battle battle;
	private boolean isTeamA;
	private BattleController roomBattleController;

	public RoomManager(Room room, int floorNumber, Player player){
		this.room = room;
		this.floorNumber = floorNumber;
		this.player = player;
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
		Team playerTeam = player.getTeam();
		Team opponentTeam = new Team();
		try{
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);
		}catch(Exception e){
			System.err.println(e);
		}
		Battle battle = new Battle(playerTeam, opponentTeam, player);
		battle.setFloorNumber(floorNumber);
		this.roomBattleController = new BattleController(player, battle, ParticipantLabel.TEAM_A);
		StrategyRandom strategyRandom = new StrategyRandom(battle);
		Thread thread = new Thread(strategyRandom);
		thread.start();
	}


	public void initializeBosslBattleRoom(){
		Team playerTeam = player.getTeam();
		Team opponentTeam = new Team();
		try{
			opponentTeam = OpponentTeamGenerator.generateRandomOpponentTeam(playerTeam);
		}catch(Exception e){
			System.err.println(e);
		}
		Battle battle = new Battle(playerTeam, opponentTeam, player);
		battle.setFloorNumber(floorNumber);
		battle.enableBossBattle();
		this.roomBattleController = new BattleController(player, battle, ParticipantLabel.TEAM_A);
		StrategyRandom strategyRandom = new StrategyRandom(battle);
		Thread thread = new Thread(strategyRandom);
		thread.start();
	}

	public void initializeRewardRoom(){
		// TODO connecter avec le systeme de rewards
	}

	public Room getRoom() {return room;}

	public void setRoom(Room room) {this.room = room;}

	public Player getPlayer() {return player;}

	public void setPlayer(Player player) {this.player = player;}

	public Battle getBattle() {return battle;}

	public void setBattle(Battle battle) {this.battle = battle;}

	public boolean isTeamA() {return isTeamA;}

	public void setTeamA(boolean teamA) {isTeamA = teamA;}

	public BattleController getRoomBattleController() {return this.roomBattleController;}

}
