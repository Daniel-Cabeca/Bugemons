package ulb.server;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.DTO.team.TeamDTO;
import ulb.communication.Messenger.SocketMessenger;
import ulb.exceptions.*;
import ulb.message.request.Request;
import ulb.message.response.StatusResponse;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.bugemon.Bugemon;
import ulb.model.reward.Reward;
import ulb.model.team.Team;
import ulb.model.tower.towerManager.TowerManager;
import ulb.service.*;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread implements ServerMessageHandler {
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

	private final SocketMessenger socketMessenger;
	private final TowerSaveService towerSaveService;
	private final TeamService teamService;
	private final SetupHandler setupHandler;
	private final PlayerInfoHandler playerInfoHandler;
	private final GameInfoHandler gameInfoHandler;
	private final GameActionsHandler gameActionsHandler;
	private final GameDataHandler gameDataHandler;
	private final SocialHandler socialHandler;
	private final TeamSaveHandler teamSaveHandler;
	private boolean stop;
	private Player player;
	private Battle battle;
	private Battle.ParticipantLabel teamLabel;
	private TowerManager towerManager;
	private boolean isGameTower;
	private Optional<Bugemon> pendingLevelUpBugemon;
	private List<Reward> pendingLevelUpRewards;

	public ClientHandler(SocketMessenger messenger, AbilityService abilityService, BugemonService bugemonService,
						 ItemService itemService, AccountService accountService, ChatService chatService,
						 TeamService teamService, InventoryService inventoryService, TowerSaveService towerSaveService
			, MultiBattleService multiBattleService) {
		this.socketMessenger = messenger;
		this.stop = false;
		this.towerSaveService = towerSaveService;
		this.teamService = teamService;

		this.setupHandler = new SetupHandler(this, accountService, itemService, inventoryService, bugemonService,
				teamService, towerSaveService, multiBattleService);
		this.gameInfoHandler = new GameInfoHandler(this, towerSaveService);
		this.gameActionsHandler = new GameActionsHandler(this, inventoryService, multiBattleService);
		this.socialHandler = new SocialHandler(this, accountService, chatService, multiBattleService);
		this.playerInfoHandler = new PlayerInfoHandler(this, accountService);
		this.gameDataHandler = new GameDataHandler(this, bugemonService, abilityService, itemService);
		this.teamSaveHandler = new TeamSaveHandler(this, teamService);
	}

    /**
	 * Resets all runtime session state (battle, tower, team label, etc.).
	 * Used when switching game modes.
     */
	void resetGameSessionState() { // package-private
		this.battle = null;
		this.teamLabel = null;
		this.towerManager = null;
		this.isGameTower = false;
		clearPendingLevelUpState();
	}

    /**
     * Clears stored pending level-up data (bugemon + rewards).
     */
	void clearPendingLevelUpState() {
		this.pendingLevelUpBugemon = Optional.empty();
		this.pendingLevelUpRewards = null;
	}

	public Player getPlayer() { return this.player; }

	public void setPlayer(Player player) { this.player = player; }

	public Battle getBattle() { return this.battle; }

	public void setBattle(Battle battle) { this.battle = battle; }

	public TowerManager getTowerManager() { return this.towerManager; }

	public void setTowerManager(TowerManager towerManager) { this.towerManager = towerManager; }

	public boolean isGameTower() { return this.isGameTower; }

	public Battle.ParticipantLabel getTeamLabel() { return this.teamLabel; }

	public void setTeamLabel(Battle.ParticipantLabel label) { this.teamLabel = label; }

	public Optional<Bugemon> getPendingLevelUpBugemon() { return this.pendingLevelUpBugemon; }

	public void setPendingLevelUpBugemon(Bugemon pendingLevelUpBugemon) {
		this.pendingLevelUpBugemon = Optional.of(pendingLevelUpBugemon);
	}

	public List<Reward> getPendingLevelUpRewards() { return this.pendingLevelUpRewards; }

	public void setPendingLevelUpRewards(List<Reward> rewards) { this.pendingLevelUpRewards = rewards; }

	public void setTeam(Team team) { this.player.setTeam(team); }

	public void setGameMode(boolean isGameTower) { this.isGameTower = isGameTower; }

    /**
     * Main thread loop. Continuously receives and processes client messages until stopped.
     */
	@Override
	public void run() {
		while (!this.stop) {
			handleMessage();
		}
		end();
	}

	/**
	 * Reads the socket and handles the received message
	 */
	private void handleMessage() {
		Request message = receiveMessage();
		if (message == null) {
			return;
		}
		try {
			message.dispatch(this);
		} catch (UserFacingException e) {
			sendErrorMessage(e.getMessage());
		} catch (DataAccessException e) {
			LOGGER.log(Level.SEVERE, "Data access error while handling a client message : " + e.getMessage());
			sendErrorMessage("A data access error occurred while handling a client message.");
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Unexpected error while handling a client message : " + e.getMessage());
			sendErrorMessage("An unexpected server error occurred while handling a client message.");
		}
	}

    /**
	 * Receives a request from the client socket.
	 *
	 * @return the received request, or null if invalid or communication failed
     */
	public Request receiveMessage() {
		try {
			Serializable received = this.socketMessenger.receiveMessage();

			if (received instanceof Request message) {
				return message;
			}

			return null;
		} catch (CommunicationException e) {
			handleCommunicationFailure("Communication with client has been interrupted.");
		}

		return null;
	}

	/**
	 * Sends an error message to the client.
	 *
	 * @param errorMessage message describing the error
	 */
	public void sendErrorMessage(String errorMessage) {
		if (this.stop) {
			return;
		}

		try {
			this.socketMessenger.sendMessage(new StatusResponse(false, errorMessage));
		} catch (CommunicationException e) {
			handleCommunicationFailure("Impossible to send error message to client.");
		}
	}

    /**
	 * Sends a success status response to the client.
     */
	public void sendSuccessMessage() {
		sendMessage(new StatusResponse(true));
	}

    /**
	 * Sends a serialized message to the client
	 *
     * @param message message to be sent
     */
	public void sendMessage(Serializable message) {
		if (this.stop) {
			return;
		}

		try {
			this.socketMessenger.sendMessage(message);
		} catch (CommunicationException e) {
			handleCommunicationFailure("Impossible to send message to client.");
		}
	}

    /**
	 * Handles communication failure by logging and stopping the client handler
	 *
     * @param message log message
     */
	private void handleCommunicationFailure(String message) {
		LOGGER.log(Level.INFO, message);
		stopProcess();
	}

    /**
     * Stops the client handler
     */
	public void stopProcess() {
		this.stop = true;
	}

    /**
     * Closes the socket connection
     */
	public void end() {
		this.socketMessenger.close();
	}

	/**
	 * Moves player to a specific tower room if in tower mode
	 *
	 * @param targetRoomId the id of the target room
	 * @throws LoadException if room cannot be loaded
	 * @throws EntityNotFoundException if room does not exist
	 */
	public void nextTowerRoom(int targetRoomId) throws LoadException, EntityNotFoundException {
		if (!isGameTower) {
			return;
		}
		this.towerManager.moveToRoom(targetRoomId);
		if (towerManager.getCurrentRoomId() == targetRoomId) {
			this.battle = this.towerManager.getCurrentBattle();
		}
	}

    /**
	 * Checks if current room matches given id
	 *
     * @param targetRoomId room id to compare
     * @return true if equal
     */
	public boolean isCurrentRoomIdEqual(int targetRoomId) {
		if (!isGameTower) {
			return false;
		}
		return this.towerManager.getCurrentRoomId() == targetRoomId;
	}

    /**
	 * Finished tower run and clears saved tower state
	 *
     * @throws DataAccessException if tower data cannot de deleted
     */
	void finishTower() throws DataAccessException {
		if (!isGameTower) {
			return;
		}
		this.towerSaveService.deleteTowerInfo(player);
		try {
			this.teamService.deleteTowerTeam(player);
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Failed to delete the TowerTeam");
		}
		this.towerManager = null;
		this.isGameTower = false;
		clearPendingLevelUpState();
	}

	// SETUP

	@Override
	public void setupMultiBattle(PlayerDTO opponent, List<BugemonDTO> bugemons) throws DataAccessException { setupHandler.setupMultiBattle(opponent, bugemons); }

	@Override
	public void registerPlayer(PlayerRegisterDTO playerRegisterDTO, boolean isLogin) throws DataAccessException { setupHandler.registerPlayer(playerRegisterDTO, isLogin); }

	@Override
	public void setupNormalMode() throws DataAccessException { setupHandler.setupNormalMode(); }

	@Override
	public void setupTeam(List<BugemonDTO> bugemons) throws DataAccessException { setupHandler.setupTeam(bugemons); }

	@Override
	public void setupTowerMode(boolean isNewTower) throws DataAccessException { setupHandler.setupTowerMode(isNewTower); }


	// PLAYER INFO

	@Override
	public void getPlayerInfo(String username) throws UserFacingException, DataAccessException { playerInfoHandler.getPlayerInfo(username); }

	@Override
	public void getPlayerInventory(String username) throws UserFacingException, DataAccessException { playerInfoHandler.getPlayerInventory(username); }

	@Override
	public void getPlayerTeam(String username) throws UserFacingException, DataAccessException { playerInfoHandler.getPlayerTeam(username); }


	// GAME INFO

	@Override
	public void checkGameFinished() throws DataAccessException { gameInfoHandler.checkGameFinished(); }

	@Override
	public void checkUsableItems(List<ItemDTO> items) throws DataAccessException { gameInfoHandler.checkUsableItems(items); }

	@Override
	public void getAbilityEffectiveness(BugemonDTO bugemonDTO, List<AbilityDTO> abilities) throws DataAccessException { gameInfoHandler.getAbilityEffectiveness(bugemonDTO, abilities); }

	@Override
	public void getActiveBugemons() throws DataAccessException { gameInfoHandler.getActiveBugemons(); }

	@Override
	public void getBattleEndInfo() throws DataAccessException { gameInfoHandler.getBattleEndInfo(); }

	@Override
	public void getBattleState() throws DataAccessException { gameInfoHandler.getBattleState(); }

	@Override
	public void getLevelUpInfo() throws UserFacingException, DataAccessException { gameInfoHandler.getLevelUpInfo(); }

	@Override
	public void getLogs(boolean clearLogs) throws DataAccessException { gameInfoHandler.getLogs(clearLogs); }

	@Override
	public void getNextWindow() throws DataAccessException { gameInfoHandler.getNextWindow(); }

	@Override
	public void getTowerInfo() throws DataAccessException { gameInfoHandler.getTowerInfo(); }

	@Override
	public void getTowerSavedInfo() throws DataAccessException { gameInfoHandler.getTowerSavedInfo(this.player); }

	// GAME ACTIONS

	@Override
	public void abandonTower() throws DataAccessException { gameActionsHandler.abandonTower(); }

	@Override
	public void chooseAbilityReward(BugemonDTO bugemonDTO, AbilityDTO oldAbilityDTO, AbilityDTO newAbilityDTO) throws UserFacingException, DataAccessException { gameActionsHandler.chooseAbilityReward(bugemonDTO, oldAbilityDTO, newAbilityDTO); }

	@Override
	public void chooseItemReward(ItemDTO itemDTO) throws DataAccessException { gameActionsHandler.chooseItemReward(itemDTO); }

	@Override
	public void chooseLevelUpReward(RewardDTO rewardDTO) throws UserFacingException, DataAccessException { gameActionsHandler.chooseLevelUpReward(rewardDTO); }

	@Override
	public void chooseStatReward(BugemonDTO bugemonDTO) throws UserFacingException, DataAccessException { gameActionsHandler.chooseStatReward(bugemonDTO); }

	@Override
	public void chooseTowerRoom(int roomId) throws DataAccessException { gameActionsHandler.chooseTowerRoom(roomId); }

	@Override
	public void readyToPlay() { gameActionsHandler.readyToPlay(); }

	@Override
	public void chooseRandomAction() { gameActionsHandler.chooseRandomAction(); }

	@Override
	public void chooseRunAction() throws DataAccessException { gameActionsHandler.chooseRunAction(); }

	@Override
	public void startMultiBattle(PlayerDTO opponentDTO) throws UserFacingException { gameActionsHandler.startMultiBattle(opponentDTO); }

	@Override
	public void quitMultiBattle(PlayerDTO opponentDTO) throws UserFacingException { gameActionsHandler.quitMultiBattle(opponentDTO); }

	@Override
	public void chooseSwapBugemonAction(BugemonDTO bugemonDTOToSwap) throws UserFacingException { gameActionsHandler.chooseSwapBugemonAction(bugemonDTOToSwap); }

	@Override
	public void chooseUseAbilityAction(AbilityDTO abilityDTO) throws DataAccessException { gameActionsHandler.chooseUseAbilityAction(abilityDTO); }

	@Override
	public void chooseUseItemAction(ItemDTO itemDTO) throws DataAccessException { gameActionsHandler.chooseUseItemAction(itemDTO); }


	// GAME DATA

	@Override
	public void getAllBugemonSpecies() throws DataAccessException { gameDataHandler.getAllBugemonSpecies(); }

	@Override
	public void getRandomAbility(BugemonDTO bugemonDTO) throws DataAccessException { gameDataHandler.getRandomAbility(bugemonDTO); }

	@Override
	public void getRandomItem() throws DataAccessException { gameDataHandler.getRandomItem(); }


	// SOCIAL

	@Override
	public void acceptBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.acceptBattleRequest(senderUsername, receiverUsername); }

	@Override
	public void acceptFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.acceptFriendRequest(senderUsername, receiverUsername); }

	@Override
	public void declineBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.declineBattleRequest(senderUsername, receiverUsername); }

	@Override
	public void declineFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.declineFriendRequest(senderUsername, receiverUsername); }

	@Override
	public void getBattleRequests(String username) throws DataAccessException { socialHandler.getBattleRequests(username); }

	@Override
	public void getMultiBattleStatus(int userId1, int userId2) throws DataAccessException { socialHandler.getMultiBattleStatus(userId1, userId2); }

	@Override
	public void getChatMessages(String usernameA, String usernameB) throws DataAccessException { socialHandler.getChatMessages(usernameA, usernameB); }

	@Override
	public void getFriendRequests(String username) throws DataAccessException { socialHandler.getFriendRequests(username); }

	@Override
	public void getFriendsList(String username) throws DataAccessException { socialHandler.getFriendsList(username); }

	@Override
	public void getLeaderboard() throws DataAccessException { socialHandler.getLeaderboard(); }

	@Override
	public void sendBattleRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException { socialHandler.sendBattleRequest(senderUsername, receiverUsername); }

	@Override
	public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws DataAccessException { socialHandler.sendChatMessage(senderUsername, receiverUsername, content); }

	@Override
	public void sendFriendRequest(String senderUsername, String receiverUsername) throws UserFacingException,
			DataAccessException { socialHandler.sendFriendRequest(senderUsername, receiverUsername); }


	// TEAM SAVE

	@Override
	public void getSavedTeams() throws DataAccessException { teamSaveHandler.getSavedTeams(); }

	@Override
	public void saveTeam(TeamDTO teamDTO) throws UserFacingException, DataAccessException { teamSaveHandler.saveTeam(teamDTO); }


}
