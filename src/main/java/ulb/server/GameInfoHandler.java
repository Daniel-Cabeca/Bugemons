package ulb.server;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import ulb.mapper.ability.AbilityMapper;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.mapper.item.ItemMapper;
import ulb.mapper.reward.RewardMapper;
import ulb.message.response.gameInfo.*;
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

import java.util.*;

public class GameInfoHandler {
	ClientHandler clientHandler;
	TowerSaveService towerSaveService;

	public GameInfoHandler(ClientHandler clientHandler, TowerSaveService towerSaveService) {
		this.clientHandler = clientHandler;
		this.towerSaveService = towerSaveService;
	}

	public void checkGameFinished() {
		Battle battle = clientHandler.getBattle();

		clientHandler.sendMessage(new GameFinishedResponse(battle.isGameFinished()));
	}

	public void checkUsableItems(List<ItemDTO> items) throws DataAccessException {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		Map<String, Boolean> usableItems = new HashMap<>();

		for (ItemDTO itemDTO : items) {
			Item item = ItemMapper.toEntity(itemDTO);
			usableItems.put(itemDTO.id(), battle.checkItem(item, teamLabel));
		}

		clientHandler.sendMessage(new UsableItemsResponse(usableItems));
	}

	public void getAbilityEffectiveness(BugemonDTO bugemonDTO, List<AbilityDTO> abilities) throws DataAccessException {
		Map<AbilityDTO, String> effectiveness = new HashMap<>();
		Bugemon bugemonTarget = BugemonMapper.toEntity(bugemonDTO);

		for (AbilityDTO abilityDTO : abilities) {
			Ability ability = AbilityMapper.toEntity(abilityDTO);
			String effectivenessMessage = ability.getEffectivenessMessage(bugemonTarget);
			effectiveness.put(abilityDTO, effectivenessMessage);
		}

		clientHandler.sendMessage(new AbilityEffectivenessResponse(effectiveness));
	}

	public void getActiveBugemons() throws DataAccessException {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		if (battle == null) {
			throw new DataAccessException("The battle has not been created");
		}
		Bugemon selfActive = battle.getActiveBugemon(teamLabel);
		Bugemon opponentActive = battle.getActiveBugemon(battle.getOpponentTeamLabel(teamLabel));

		clientHandler.sendMessage(new ActiveBugemonsResponse(BugemonMapper.toDTO(selfActive),
				BugemonMapper.toDTO(opponentActive)));
	}

	public void getBattleEndInfo() throws DataAccessException {
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

		if (isGameTower && towerManager != null) {
			towerManager.setFledBattle(false);
		}

		clientHandler.clearPendingLevelUpState();
		clientHandler.sendMessage(new BattleEndInfoResponse(isWin, gainedXp, multiplayerBattle));
	}

	public void getBattleState() {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		clientHandler.sendMessage(new BattleStateResponse(battle.getState(teamLabel)));
	}

	public void getLevelUpInfo() throws UserFacingException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();
		Optional<Bugemon> pendingLevelUpBugemon = clientHandler.getPendingLevelUpBugemon();
		List<Reward> pendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();

		if (battle == null || player == null || player.getTeam() == null) {
			throw new UserFacingException("No pending level up information available");
		}

		Optional<Bugemon> currentBugemon = player.getTeam().getFirstLevelUpBugemon();
		if (currentBugemon.isEmpty()) {
			clientHandler.clearPendingLevelUpState();
			throw new UserFacingException("No bugemon requires a level up reward");
		}

		if (pendingLevelUpBugemon.isEmpty() || pendingLevelUpRewards == null || pendingLevelUpBugemon.isEmpty() || !pendingLevelUpBugemon.get().getSpeciesId().equals(currentBugemon.get().getSpeciesId())) {
			clientHandler.setPendingLevelUpBugemon(currentBugemon.get());
			clientHandler.setPendingLevelUpRewards(new ArrayList<>(battle.computeRewards(currentBugemon.get())));
		}
		List<Reward> updatedPendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();

		List<RewardDTO> rewardDTOs = new ArrayList<>();
		for (Reward reward : updatedPendingLevelUpRewards) {
			rewardDTOs.add(RewardMapper.toDTO(reward));
		}

		clientHandler.sendMessage(new LevelUpInfoResponse(BugemonMapper.toDTO(currentBugemon.get()), rewardDTOs));
	}

	public void getLogs(boolean clearLogs) {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		int selfHpAfterFirstAction = battle.getHpAfterFirstActionSelf(teamLabel);
		int opponentHpAfterFirstAction = battle.getHpAfterFirstActionOpponent(teamLabel);

		List<String> logs = new ArrayList<>(battle.getLogMsg());

		if (clearLogs) {
			battle.clearLogMsg();
		}

		clientHandler.sendMessage(new LogsResponse(List.of(selfHpAfterFirstAction, opponentHpAfterFirstAction), logs));
	}

	public void getNextWindow() throws DataAccessException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();
		TowerManager towerManager = clientHandler.getTowerManager();
		boolean isGameTower = clientHandler.isGameTower();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		WindowType nextWindow = WindowType.MAIN_MENU;

		if (player != null && player.getTeam().getLevelUpBugemonNumber() > 0) {
			nextWindow = WindowType.LEVEL_UP;
			clientHandler.sendMessage(new NextWindowResponse(nextWindow));
			return;
		}

		if (isGameTower) {
			if (battle != null && battle.isGameFinished()) {
				boolean won = battle.getState(teamLabel) == BattleState.WON;

				if (won) {
					try {
						towerManager.setCurrentRoomCompleted(true);
					} catch (Exception e) {
						throw new DataAccessException("The room cannot be completed");
					}
					battle.resetFightStats();
					RoomType currentRoomType = towerManager.getCurrentRoomType();

					if (currentRoomType == RoomType.BOSS) {
						try {
							towerManager.nextFloor();
						} catch (Exception e) {
							throw new DataAccessException("The next floor cannot be selected");
						}

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

				clientHandler.sendMessage(new NextWindowResponse(nextWindow));
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

			clientHandler.sendMessage(new NextWindowResponse(nextWindow));
			return;
		}

		if (battle == null) {
			clientHandler.sendMessage(new NextWindowResponse(nextWindow));
			return;
		}

		BattleParticipant battleParticipantSelf = battle.getParticipant(teamLabel);

		if (battleParticipantSelf.getAction() instanceof Run) {
			nextWindow = WindowType.MAIN_MENU;
			clientHandler.sendMessage(new NextWindowResponse(nextWindow));
			return;
		}

		nextWindow = battle.isGameFinished() ? WindowType.MAIN_MENU : WindowType.GAME;
		clientHandler.sendMessage(new NextWindowResponse(nextWindow));
	}

	public void getTowerInfo() throws DataAccessException {
		TowerManager towerManager = clientHandler.getTowerManager();
		boolean isGameTower = clientHandler.isGameTower();

		if (!isGameTower) {
			throw new DataAccessException("Cannot get Tower info if the game isn't in Tower mode");
		}
		int towerFloorNumber = towerManager.getFloorNumber();
		int towerRoomNumber = towerManager.getCurrentRoomId();
		boolean fledBattle = towerManager.hasFledBattle();

		List<Integer> clearedRooms;
		try {
			clearedRooms = towerManager.getCurrentFloorClearedRooms();
		} catch (Exception e) {
			throw new DataAccessException("Cannot get the cleared room for the current floor");
		}

		clientHandler.sendMessage(new TowerInfoResponse(towerFloorNumber, towerRoomNumber, clearedRooms, fledBattle));
		towerManager.setFledBattle(false); // resets the flag after sending it
	}

	public void getTowerSavedInfo(Player player) throws DataAccessException {
		boolean isTowerSaved = this.towerSaveService.isTowerSaved(player);
		clientHandler.sendMessage(new TowerSavedInfoResponse(isTowerSaved));
	}
}
