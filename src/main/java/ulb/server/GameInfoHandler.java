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
import ulb.model.action.RunAction;
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

/**
 * Handles requests for current game-related information, such as next window, battle end, battle state, or tower state.
 */
public class GameInfoHandler {
	ClientHandler clientHandler;
	TowerSaveService towerSaveService;

	/**
	 * Creates a game info handler for the given client and services.
	 *
	 * @param clientHandler the client handler owning this session
	 * @param towerSaveService service for tower save state
	 */
	public GameInfoHandler(ClientHandler clientHandler, TowerSaveService towerSaveService) {
		this.clientHandler = clientHandler;
		this.towerSaveService = towerSaveService;
	}

    /**
     * Checks if the current battle is over and sends the answer to the client.
     */
	public void checkGameFinished() {
		Battle battle = clientHandler.getBattle();
		clientHandler.sendMessage(new GameFinishedResponse(battle.isGameFinished()));
	}

    /**
	 * Checks which items can be currently used by the player and sends the answer to the client.
	 *
     * @param items the list of ItemDTOs to be checked
     * @throws DataAccessException if the item can't be mapped
     */
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

    /**
	 * Sends the effectiveness message for every ability to the client.
	 *
     * @param bugemonDTO the opponent's bugemon
     * @param abilities the list of AbilityDTOs for which the effectiveness message is needed
     * @throws DataAccessException if the ability or bugemon can't be mapped
     */
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

    /**
	 * Sends the player's and the opponent's currently active bugemons to the client.
	 *
     * @throws DataAccessException if there is no battle
     */
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

    /**
	 * Collects and sends battle end information to the client, including whether the player won, XP gained,
	 * and whether the battle was multiplayer. Clears battle-related state and updates tower state if needed.
	 *
     * @throws DataAccessException if tower cleanup fails
     */
	public void getBattleEndInfo() throws DataAccessException {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();
		boolean isGameTower = clientHandler.isGameTower();
		TowerManager towerManager = clientHandler.getTowerManager();

		boolean multiplayerBattle = battle.getMultiplayerBattle();

		boolean isWin = battle != null && battle.getState(teamLabel) == BattleState.WON;
		int gainedXp = 0;

		if (isWin) {
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

    /**
     * Sends the current battle state to the client.
     */
	public void getBattleState() {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();
		clientHandler.sendMessage(new BattleStateResponse(battle.getState(teamLabel)));
	}

    /**
	 * Checks if there is a pending level up state and sets the correct bugemon and rewards.
	 *
     * @param battle the battle during which a bugemon leveled up
     * @param current the bugemon who leveled up
     */
	private void ensurePendingLevelUpState(Battle battle, Bugemon current){
		Optional<Bugemon> pending = clientHandler.getPendingLevelUpBugemon();
		List<Reward> pendingRewards = clientHandler.getPendingLevelUpRewards();

		boolean isPendingLevelUp = pending.isEmpty() || pendingRewards == null
				|| !pending.get().getSpeciesId().equals(current.getSpeciesId());

		if (isPendingLevelUp) {
			clientHandler.setPendingLevelUpBugemon(current);
			clientHandler.setPendingLevelUpRewards(new ArrayList<>(battle.computeRewards(current)));
		}
	}

    /**
	 * Sends level up info (the bugemon who leveled up and the generated rewards) to the client.
	 *
     * @throws UserFacingException if no level-up information is available or no bugemon requires a level-up reward
     */
	public void getLevelUpInfo() throws UserFacingException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();

		if (battle == null || player == null || player.getTeam() == null) {
			throw new UserFacingException("No pending level up information available");
		}

		Optional<Bugemon> currentBugemon = player.getTeam().getFirstLevelUpBugemon();
		if (currentBugemon.isEmpty()) {
			clientHandler.clearPendingLevelUpState();
			throw new UserFacingException("No bugemon requires a level up reward");
		}

		ensurePendingLevelUpState(battle, currentBugemon.get());

		List<Reward> updatedPendingLevelUpRewards = clientHandler.getPendingLevelUpRewards();
		List<RewardDTO> rewardDTOs = new ArrayList<>();
		for (Reward reward : updatedPendingLevelUpRewards) {
			rewardDTOs.add(RewardMapper.toDTO(reward));
		}

		clientHandler.sendMessage(new LevelUpInfoResponse(BugemonMapper.toDTO(currentBugemon.get()), rewardDTOs));
	}

    /**
	 * Sends the log messages after an action to the player, and clears them based on the flag.
	 *
     * @param clearLogs whether battle logs should be cleared after sending
     */
	public void getLogs(boolean clearLogs) {
		Battle battle = clientHandler.getBattle();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();

		int selfHpAfterFirstAction = battle.getHpAfterFirstActionSelf(teamLabel);
		int opponentHpAfterFirstAction = battle.getHpAfterFirstActionOpponent(teamLabel);

		List<String> logs = new ArrayList<>(battle.getLogMsgIfNotCleard(teamLabel));

		if (clearLogs) {
			battle.clearLogMsg(teamLabel);
		}
		clientHandler.sendMessage(new LogsResponse(List.of(selfHpAfterFirstAction, opponentHpAfterFirstAction), logs));
	}

    /**
	 * Resolves the next window to be shown in tower mode.
	 *
     * @param battle the current battle after which another window has to be shown
     * @return the WindowType indicating which window has to be shown
     * @throws DataAccessException if the next window cannot be resolved
     */
	private WindowType resolveNextWindowTowerMode(Battle battle) throws DataAccessException {
		WindowType windowType = WindowType.MAIN_MENU;
		TowerManager towerManager = clientHandler.getTowerManager();
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();
		if (battle != null && battle.isGameFinished()) {
			boolean won = battle.getState(teamLabel) == BattleState.WON;
			if (won) {
				windowType = resolveNextWindowAfterWinInTower(towerManager, battle);
			} else {
				clientHandler.finishTower();
			}
		} else {
			windowType = resolveNextWindowInTowerFloor(towerManager);
		}

		return windowType;
	}

    /**
	 * Resolves the next window to be shown after winning a battle in tower mode.
	 *
     * @param towerManager the tower manager
     * @param battle the battle that was won
     * @return the WindowType indicating which window has to be shown
     * @throws DataAccessException if the room cannot be completed or the next floor cannot be selected
     */
	private WindowType resolveNextWindowAfterWinInTower(TowerManager towerManager, Battle battle) throws DataAccessException {
		WindowType nextWindow = WindowType.FLOOR;
		try {
			towerManager.setCurrentRoomCompleted(true);
		} catch (Exception e) {
			throw new DataAccessException("The room cannot be completed");
		}
		battle.resetAllFightStats();
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
		}
		return nextWindow;
	}

    /**
	 * Resolves the next window to be shown based on the selected room in tower mode.
	 *
     * @param towerManager the tower manager
     * @return the WindowType indicating which window has to be shown
     */
	private WindowType resolveNextWindowInTowerFloor(TowerManager towerManager) {
		WindowType nextWindow = WindowType.MAIN_MENU;
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
		return nextWindow;
	}

    /**
	 * Resolves the next window to be shown in classic/auto mode.
	 *
     * @param battle the current battle
     * @return the WindowType indicating which window has to be shown
     */
	private WindowType resolveNextWindowInClassicMode(Battle battle) {
		ParticipantLabel teamLabel = clientHandler.getTeamLabel();
		BattleParticipant battleParticipantSelf = battle.getParticipant(teamLabel);

		if (battleParticipantSelf.getAction() instanceof RunAction) {
			return WindowType.MAIN_MENU;
		}
		return battle.isGameFinished() ? WindowType.MAIN_MENU : WindowType.GAME;
	}

    /**
	 * Sends the next window to be shown to the client.
	 *
     * @throws DataAccessException if resolving the next window fails
     */
	public void getNextWindow() throws DataAccessException {
		Player player = clientHandler.getPlayer();
		Battle battle = clientHandler.getBattle();
		boolean isGameTower = clientHandler.isGameTower();
		WindowType nextWindow;

		if (player != null && player.getTeam().getLevelUpBugemonNumber() > 0) {
			nextWindow = WindowType.LEVEL_UP;
		} else if (isGameTower) {
			nextWindow = resolveNextWindowTowerMode(battle);
		} else if (battle == null) {
			nextWindow = WindowType.MAIN_MENU;
		} else {
			nextWindow = resolveNextWindowInClassicMode(battle);
		}

		clientHandler.sendMessage(new NextWindowResponse(nextWindow));
	}

    /**
	 * Sends current information about the tower (floor, room, cleared rooms and if the player fled a battle)
	 * to the client.
	 *
     * @throws DataAccessException if the game is not in tower mode or if cleared room information cannot be retrieved
     */
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

    /**
	 * Checks if a tower save for the player exists in the db and sends the answer to the client.
	 *
     * @param player the player whose tower save is checked
     * @throws DataAccessException if the database lookup fails
     */
	public void getTowerSavedInfo(Player player) throws DataAccessException {
		boolean isTowerSaved = this.towerSaveService.isTowerSaved(player);
		clientHandler.sendMessage(new TowerSavedInfoResponse(isTowerSaved));
	}
}
