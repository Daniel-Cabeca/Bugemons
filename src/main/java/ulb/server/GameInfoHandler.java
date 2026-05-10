package ulb.server;

import ulb.DTO.bugemon.BugemonDTO;
import ulb.exceptions.DataAccessException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.reward.RewardMapper;
import ulb.message.serverToClient.gameInfo.*;
import ulb.model.Player;
import ulb.model.ability.Ability;
import ulb.model.action.Run;
import ulb.model.battle.Battle;
import ulb.model.battle.Battle.ParticipantLabel;
import ulb.model.battle.BattleParticipant;
import ulb.model.battle.BattleState;
import ulb.model.bugemon.Bugemon;
import ulb.model.item.Item;
import ulb.model.reward.Reward;
import ulb.model.tower.Room;
import ulb.model.tower.RoomType;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.TowerSaveService;

public class GameInfoHandler {
    ClientHandler clientHandler;
	TowerSaveService towerSaveService;

    public GameInfoHandler(ClientHandler clientHandler, TowerSaveService towerSaveService) {
		this.clientHandler = clientHandler;
		this.towerSaveService = towerSaveService;
    }

    public void checkGameFinished() throws DataAccessException{
        Battle battle = clientHandler.getBattle();

		clientHandler.sendMessage(new GameFinishedMessage(battle.isGameFinished()));
	}

	public void checkUsableItems(List<ItemDTO> items) throws DataAccessException{
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Map<String, Boolean> usableItems = new HashMap<String, Boolean>();

		for (ItemDTO itemDTO : items){
			Item item = ItemMapper.toEntity(itemDTO);
			usableItems.put(itemDTO.id(), battle.checkItem(item, teamLabel));
		}

        clientHandler.sendMessage(new UsableItemsMessage(usableItems));
	}

	public void getAbilityEffectiveness(BugemonDTO bugemonDTO, List<AbilityDTO> abilities) throws DataAccessException{
		Map<AbilityDTO, String> effectiveness = new HashMap<AbilityDTO, String>();
		Bugemon bugemonTarget = BugemonMapper.toEntity(bugemonDTO);
		
		for (AbilityDTO abilityDTO : abilities){
			Ability ability = AbilityMapper.toEntity(abilityDTO);
			String effectivenessMessage = ability.getEffectivenessMessage(bugemonTarget);
			effectiveness.put(abilityDTO, effectivenessMessage);
		}

		clientHandler.sendMessage(new AbilityEffectivenessMessage(effectiveness));
	}

    public void getActiveBugemons() throws DataAccessException{
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		if (battle == null){
			clientHandler.sendErrorMessage("The battle has not been created");
			return;
		}
		Bugemon selfActive = battle.getActiveBugemon(teamLabel);
		Bugemon opponentActive = battle.getActiveBugemon(battle.getOpponentTeamLabel(teamLabel)); 
		
		clientHandler.sendMessage(new ActiveBugemonsMessage(BugemonMapper.toDTO(selfActive), BugemonMapper.toDTO(opponentActive)));
	}

	public void getBattleEndInfo() throws DataAccessException{
		Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();
		boolean isGameTower = clientHandler.isGameTower();
		TowerManager towerManager = clientHandler.getTowerManager();

		boolean multiplayerBattle = battle.getMultiplayerBattle();

		boolean isWin = battle != null && battle.getState(teamLabel) == BattleState.WON;
		int gainedXp = 0;

		if (isWin && battle != null) {
			gainedXp = battle.computeTotalXP(battle.getTeam(battle.getOpponentTeamLabel(teamLabel)));
		}

		if (battle != null && battle.isGameFinished()) {
			clientHandler.setBattle(null);
		}

		if (isGameTower && towerManager != null && towerManager.isTowerCompleted()) {
			clientHandler.finishTower();
		}

		clientHandler.clearPendingLevelUpState();
		clientHandler.sendMessage(new BattleEndInfoMessage(isWin, gainedXp, multiplayerBattle));
	}

	public void getBattleState() throws DataAccessException{
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		clientHandler.sendMessage(new BattleStateMessage(battle.getState(teamLabel)));
	}

	public void getLevelUpInfo() throws DataAccessException{
        Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        Bugemon pendingLevelUpBugemon = clientHandler.getPendingLevelUpBugemon();
        List<Reward> pendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();

		if (battle == null || player == null || player.getTeam() == null) {
			clientHandler.sendErrorMessage("No pending level up information available");
			return;
		}

		Bugemon currentBugemon = player.getTeam().getFirstLevelUpBugemon();
		if (currentBugemon == null) {
			clientHandler.clearPendingLevelUpState();
			clientHandler.sendErrorMessage("No bugemon requires a level up reward");
			return;
		}

		if (pendingLevelUpBugemon == null
				|| pendingLevelUpRewards == null
				|| !pendingLevelUpBugemon.getSpeciesId().equals(currentBugemon.getSpeciesId())) {
			clientHandler.setPendingLevelUpBugemon(currentBugemon);
			clientHandler.setPendingLevelUpRewards(new ArrayList<>(battle.computeRewards(currentBugemon)));
		}
        List<Reward> updatedPendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();

		List<RewardDTO> rewardDTOs = new ArrayList<>();
		for (Reward reward : updatedPendingLevelUpRewards) {
			rewardDTOs.add(RewardMapper.toDTO(reward));
		}

		clientHandler.sendMessage(new LevelUpInfoMessage(BugemonMapper.toDTO(currentBugemon), rewardDTOs));
	}

    public void getLogs(boolean clearLogs) throws DataAccessException{
        Battle battle = clientHandler.getBattle();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		int selfHpAfterFirstAction = battle.getHpAfterFirstActionSelf(teamLabel);
		int opponentHpAfterFirstAction = battle.getHpAfterFirstActionOpponent(teamLabel);
		
		List<String> logs = new ArrayList<String>(battle.getLogMsg());
		
		if (clearLogs){
			battle.clearLogMsg();
		}

		clientHandler.sendMessage(new LogsMessage(List.of(selfHpAfterFirstAction, opponentHpAfterFirstAction), logs));
	}

	public void getNextWindow() throws DataAccessException{
		Player player = clientHandler.getPlayer();
        Battle battle = clientHandler.getBattle();
        TowerManager towerManager = clientHandler.getTowerManager();
        boolean isGameTower = clientHandler.isGameTower();
        ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		BattleParticipant battleParticipantSelf = battle.getParticipant(teamLabel);

		WindowType nextWindow = WindowType.MAIN_MENU;

		if (player != null && player.getTeam().getLevelUpBugemonNumber() > 0){
			nextWindow = WindowType.LEVEL_UP;
			clientHandler.sendMessage(new NextWindowMessage(nextWindow));
			return;
		}

		if (isGameTower){
			if (battle != null && battle.isGameFinished()) {
				boolean won = battle.getState(teamLabel) == BattleState.WON;

				if (won) {
					towerManager.setCurrentRoomCompleted(true);
					battle.resetFightStats();
					RoomType currentRoomType = towerManager.getCurrentRoomType();

					if (currentRoomType == RoomType.BOSS) {
						towerManager.nextFloor();
	
						if (!towerManager.isTowerCompleted()) {
							RoomType nextRoomType = towerManager.getCurrentRoomType();
							clientHandler.setBattle(towerManager.getCurrentBattle());
							// the final floor is just one boss battle
							nextWindow = (nextRoomType == RoomType.BOSS) ? WindowType.GAME : WindowType.NEXT_ROOM;
						}
					} else {
						nextWindow = WindowType.FLOOR;
					}
				} else {
					clientHandler.finishTower();
				}

				clientHandler.sendMessage(new NextWindowMessage(nextWindow));
				return;
			}

			Room currentRoom = towerManager.getCurrentRoomManager().getRoom();
			if (currentRoom.isRoomCompleted()) { // to avoid redoing a completed room
				nextWindow = WindowType.FLOOR;
			} else {
				switch (towerManager.getCurrentRoomType()) {
					case BATTLE:
					case BOSS:
						nextWindow = WindowType.GAME;
						break;
					case REWARD:
						nextWindow = WindowType.REWARD;
						break;
					case EMPTY:
						nextWindow = WindowType.FLOOR;
						break;
					default:
						break;
				}
			}

			clientHandler.sendMessage(new NextWindowMessage(nextWindow));
			return;
		}

		if (battle == null){
			clientHandler.sendMessage(new NextWindowMessage(nextWindow));
			return;
		}

		if (battleParticipantSelf.getAction() instanceof Run) {
			nextWindow = WindowType.MAIN_MENU;
			clientHandler.sendMessage(new NextWindowMessage(nextWindow));
			return;
		}

		nextWindow = battle.isGameFinished() ? WindowType.MAIN_MENU : WindowType.GAME;
		clientHandler.sendMessage(new NextWindowMessage(nextWindow));
	}

	public void getTowerInfo() throws DataAccessException{
        TowerManager towerManager = clientHandler.getTowerManager();
        boolean isGameTower = clientHandler.isGameTower();

		if (!isGameTower){
			clientHandler.sendErrorMessage("The game isn't in tower mode");
			return;
		}
		int towerFloorNumber = towerManager.getFloorNumber();
		int towerRoomNumber = towerManager.getCurrentRoomId();
		List<Integer> clearedRooms = towerManager.getCurrentFloorClearedRooms();

		clientHandler.sendMessage(new TowerInfoMessage(towerFloorNumber, towerRoomNumber, clearedRooms));
	}

	public void getTowerSavedInfo(Player player) throws DataAccessException{
		boolean isTowerSaved = this.towerSaveService.isTowerSaved(player);
		clientHandler.sendMessage(new TowerSavedInfoMessage(isTowerSaved));
	}
}
