package ulb.server;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.DTO.team.TeamDTO;
import ulb.communication.Messenger.SocketMessenger;
import ulb.exceptions.CommunicationException;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ulb.message.clientToServer.ClientToServerMessage;
import ulb.message.serverToClient.*;
import ulb.model.Player;
import ulb.model.battle.Battle;
import ulb.model.battle.MultiBattleSession;
import ulb.model.bugemon.Bugemon;
import ulb.model.reward.Reward;
import ulb.model.team.Team;
import ulb.service.*;
import ulb.model.tower.towerManager.TowerManager;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler extends Thread implements ServerMessageHandler{
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

    private SocketMessenger socketMessenger;
    private boolean stop;

    private Player player;

    private Battle battle;
    private Battle.ParticipantLabel teamLabel;
    private Thread opponentBot;
	private MultiBattleSession multiBattleSession = null;

	private TowerManager towerManager;
	private boolean isGameTower;

	private Bugemon pendingLevelUpBugemon;
	private List<Reward> pendingLevelUpRewards;

	private final TowerSaveService towerSaveService;
	private final TeamService teamService;

	private SetupHandler setupHandler;
	private PlayerInfoHandler playerInfoHandler;
	private GameInfoHandler gameInfoHandler;
	private GameActionsHandler gameActionsHandler;
	private GameDataHandler gameDataHandler;
	private SocialHandler socialHandler;
	private TeamSaveHandler teamSaveHandler;

	void resetGameSessionState() { // package-private
		this.battle = null;
		this.teamLabel = null;
		this.towerManager = null;
		this.isGameTower = false;
		clearPendingLevelUpState();
	}

    public ClientHandler(SocketMessenger messenger,
                         AbilityService abilityService, BugemonService bugemonService, ItemService itemService,
                         AccountService accountService, ChatService chatService, TeamService teamService, InventoryService inventoryService, TowerSaveService towerSaveService,
						 MultiBattleService multiBattleService) {
        this.socketMessenger = messenger;
		this.stop = false;
		this.towerSaveService = towerSaveService;
		this.teamService = teamService;

		this.setupHandler = new SetupHandler(this, accountService, itemService, inventoryService, bugemonService, teamService, towerSaveService, multiBattleService);
		this.gameInfoHandler = new GameInfoHandler(this, towerSaveService);
		this.gameActionsHandler = new GameActionsHandler(this, inventoryService, multiBattleService);
		this.socialHandler = new SocialHandler(this, accountService, chatService, multiBattleService);
		this.playerInfoHandler = new PlayerInfoHandler(this, accountService);
		this.gameDataHandler = new GameDataHandler(this, bugemonService, abilityService, itemService);
		this.teamSaveHandler = new TeamSaveHandler(this, teamService);
    }

	public Player getPlayer() { return this.player; }
	public Battle getBattle() { return this.battle; }
	public TowerManager getTowerManager() { return this.towerManager; }
	public boolean isGameTower() { return this.isGameTower; }
	public boolean isMultiPlayer() { return this.multiBattleSession != null; }
	public Battle.ParticipantLabel getTeamLabel() { return this.teamLabel; }
	public Thread getOpponentBot() { return this.opponentBot; }
	public Bugemon getPendingLevelUpBugemon() { return this.pendingLevelUpBugemon; }
	public List<Reward> getPendingLevelUpRewards() { return this.pendingLevelUpRewards; }

	public void setPlayer(Player player) { this.player = player; }
	public void setTeam(Team team) { this.player.setTeam(team); }
	public void setBattle(Battle battle) { this.battle = battle; }
	public void setMultiBattleSession(MultiBattleSession multiBattleSession) { this.multiBattleSession = multiBattleSession; }
	public void setTowerManager(TowerManager towerManager) { this.towerManager = towerManager; }
	public void setGameMode(boolean isGameTower) { this.isGameTower = isGameTower; }
	public void setTeamLabel(Battle.ParticipantLabel label) { this.teamLabel = label; }
	public void setOpponentBot(Thread opponentBot) { this.opponentBot = opponentBot; }
	public void setPendingLevelUpBugemon(Bugemon pendingLevelUpBugemon) { this.pendingLevelUpBugemon = pendingLevelUpBugemon; }
	public void setPendingLevelUpRewards(List<Reward> rewards) { this.pendingLevelUpRewards = rewards; }


    @Override
    public void run(){
        while (! this.stop){
            handleMessage();
        }
        end();
    }

	/**
	 * Reads the socket and handle the received message
	 */
	private void handleMessage(){
		ClientToServerMessage message = receiveMessage();
		if (message == null){
			return;
		}
		try {
			message.dispatch(this);
		} catch (UserFacingException e) {
			sendErrorMessage(e.getMessage());
		} catch (DataAccessException e) {
			LOGGER.log(Level.SEVERE, "Data access error while handling a client message.", e);
			sendErrorMessage("A data access error occurred while handling a client message.");
		} catch (RuntimeException e) {
			LOGGER.log(Level.SEVERE, "Unexpected error while handling a client message.", e);
			sendErrorMessage("An unexpected server error occurred while handling a client message.");
		}
	}

    public void stopProcess(){
        this.stop = true;
    }

    public ClientToServerMessage receiveMessage(){
		try {
			Serializable received = this.socketMessenger.receiveMessage();

			if (received instanceof ClientToServerMessage message) {
				return message;
			}

			return null;
		} catch (CommunicationException e) {
			handleCommunicationFailure("Communication with client has been interrupted.", e);
		}

		return null;
	}

	public void sendMessage(Serializable message) {
		if (this.stop) {
			return;
		}

		try {
			this.socketMessenger.sendMessage(message);
		} catch (CommunicationException e) {
			handleCommunicationFailure("Impossible to send message to client.", e);
		}
	}

	public void sendErrorMessage(String errorMessage) {
		if (this.stop) {
			return;
		}

		try {
			this.socketMessenger.sendMessage(new StatusMessage(false, errorMessage));
		} catch (CommunicationException e) {
			handleCommunicationFailure("Impossible to send error message to client.", e);
		}
	}

	private void handleCommunicationFailure(String message, CommunicationException e) {
		LOGGER.log(Level.INFO, message);
		stopProcess();
	}

    public void sendSuccessMessage(){
        sendMessage(new StatusMessage(true));
    }

    public void end(){
        this.socketMessenger.close();
    }

	/**
	 * Go to the next room if the game is in tower mode
	 */
	public boolean nextTowerRoom(int targetRoomId) throws DataAccessException {
		if (!isGameTower){
			return false;
		}
		if (this.towerManager.moveToRoom(targetRoomId)){
			this.battle = this.towerManager.getCurrentBattle();
			return true;
		}
		return false;

	}

	void finishTower() throws DataAccessException {
		if (!isGameTower){return;}
		this.towerSaveService.deleteTowerInfo(player);
		this.teamService.deleteTowerTeam(player);
		this.resetGameSessionState();
	}

	void clearPendingLevelUpState() {
		this.pendingLevelUpBugemon = null;
		this.pendingLevelUpRewards = null;
	}

	// SETUP

	@Override public void setupMultiBattle(PlayerDTO opponent, List<BugemonDTO> bugemons) throws DataAccessException { setupHandler.setupMultiBattle(opponent, bugemons); }
	@Override public void registerPlayer(PlayerRegisterDTO playerRegisterDTO, boolean isLogin) throws DataAccessException { setupHandler.registerPlayer(playerRegisterDTO, isLogin); }
	@Override public void setupNormalMode() throws DataAccessException { setupHandler.setupNormalMode(); }
	@Override public void setupTeam(List<BugemonDTO> bugemons) throws DataAccessException { setupHandler.setupTeam(bugemons); }
	@Override public void setupTowerMode(boolean isNewTower) throws DataAccessException { setupHandler.setupTowerMode(isNewTower); }


	// PLAYER INFO

	@Override public void getPlayerInfo(String username) throws DataAccessException { playerInfoHandler.getPlayerInfo(username); }
	@Override public void getPlayerInventory(String username) throws DataAccessException { playerInfoHandler.getPlayerInventory(username); }
	@Override public void getPlayerTeam() throws DataAccessException { playerInfoHandler.getPlayerTeam(); }


	// GAME INFO

	@Override public void checkGameFinished() throws DataAccessException { gameInfoHandler.checkGameFinished(); }
	@Override public void checkUsableItems(List<ItemDTO> items) throws DataAccessException { gameInfoHandler.checkUsableItems(items); }
	@Override public void getAbilityEffectiveness(BugemonDTO bugemonDTO, List<AbilityDTO> abilities) throws DataAccessException { gameInfoHandler.getAbilityEffectiveness(bugemonDTO, abilities); }
	@Override public void getActiveBugemons() throws DataAccessException { gameInfoHandler.getActiveBugemons(); }
	@Override public void getBattleEndInfo() throws DataAccessException { gameInfoHandler.getBattleEndInfo(); }
	@Override public void getBattleState() throws DataAccessException { gameInfoHandler.getBattleState(); }
	@Override public void getLevelUpInfo() throws DataAccessException { gameInfoHandler.getLevelUpInfo(); }
	@Override public void getLogs(boolean clearLogs) throws DataAccessException { gameInfoHandler.getLogs(clearLogs); }
	@Override public void getNextWindow() throws DataAccessException { gameInfoHandler.getNextWindow(); }
	@Override public void getTowerInfo() throws DataAccessException { gameInfoHandler.getTowerInfo(); }
	@Override public void getTowerSavedInfo() throws DataAccessException { gameInfoHandler.getTowerSavedInfo(this.player); }

	// GAME ACTIONS

	@Override public void abandonTower() throws DataAccessException { gameActionsHandler.abandonTower(); }
	@Override public void chooseAbilityReward(BugemonDTO bugemonDTO, AbilityDTO oldAbilityDTO, AbilityDTO newAbilityDTO) throws DataAccessException { gameActionsHandler.chooseAbilityReward(bugemonDTO, oldAbilityDTO, newAbilityDTO); }
	@Override public void chooseItemReward(ItemDTO itemDTO) throws DataAccessException { gameActionsHandler.chooseItemReward(itemDTO); }
	@Override public void chooseLevelUpReward(RewardDTO rewardDTO) throws DataAccessException { gameActionsHandler.chooseLevelUpReward(rewardDTO); }
	@Override public void chooseStatReward(BugemonDTO bugemonDTO) throws DataAccessException { gameActionsHandler.chooseStatReward(bugemonDTO); }
	@Override public void chooseTowerRoom(int roomId) throws DataAccessException { gameActionsHandler.chooseTowerRoom(roomId); }
	@Override public void chooseRandomAction() throws DataAccessException { gameActionsHandler.chooseRandomAction(); }
	@Override public void chooseRunAction() throws DataAccessException { gameActionsHandler.chooseRunAction(); }
	@Override public void startMultiBattle(PlayerDTO opponentDTO) throws DataAccessException { gameActionsHandler.startMultiBattle(opponentDTO); }
	@Override public void chooseSwapBugemonAction(BugemonDTO bugemonDTOToSwap) throws DataAccessException { gameActionsHandler.chooseSwapBugemonAction(bugemonDTOToSwap); }
	@Override public void chooseUseAbilityAction(AbilityDTO abilityDTO) throws DataAccessException { gameActionsHandler.chooseUseAbilityAction(abilityDTO); }
	@Override public void chooseUseItemAction(ItemDTO itemDTO) throws DataAccessException { gameActionsHandler.chooseUseItemAction(itemDTO); }


	// GAME DATA

	@Override public void getAllBugemonSpecies() throws DataAccessException { gameDataHandler.getAllBugemonSpecies(); }
	@Override public void getRandomAbility(BugemonDTO bugemonDTO) throws DataAccessException { gameDataHandler.getRandomAbility(bugemonDTO); }
	@Override public void getRandomItem() throws DataAccessException { gameDataHandler.getRandomItem(); }


	// SOCIAL

	@Override public void acceptBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.acceptBattleRequest(senderUsername, receiverUsername); }
	@Override public void acceptFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.acceptFriendRequest(senderUsername, receiverUsername); }
	@Override public void declineBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.declineBattleRequest(senderUsername, receiverUsername); }
	@Override public void declineFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.declineFriendRequest(senderUsername, receiverUsername); }
	@Override public void getBattleRequests(String username) throws DataAccessException { socialHandler.getBattleRequests(username); }
	@Override public void getMultiBattleStatus(int userId1, int userId2) throws DataAccessException { socialHandler.getMultiBattleStatus(userId1, userId2); }
	@Override public void getChatMessages(String usernameA, String usernameB) throws DataAccessException { socialHandler.getChatMessages(usernameA, usernameB); }
	@Override public void getFriendRequests(String username) throws DataAccessException { socialHandler.getFriendRequests(username); }
	@Override public void getFriendsList(String username) throws DataAccessException { socialHandler.getFriendsList(username); }
	@Override public void getLeaderboard() throws DataAccessException {socialHandler.getLeaderboard(); }
	@Override public void sendBattleRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.sendBattleRequest(senderUsername, receiverUsername); }
	@Override public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws DataAccessException { socialHandler.sendChatMessage(senderUsername, receiverUsername, content); }
	@Override public void sendFriendRequest(String senderUsername, String receiverUsername) throws DataAccessException { socialHandler.sendFriendRequest(senderUsername, receiverUsername); }


	// TEAM SAVE

	@Override public void getSavedTeams() throws DataAccessException { teamSaveHandler.getSavedTeams(); }
	@Override public void saveTeam(TeamDTO teamDTO) throws DataAccessException { teamSaveHandler.saveTeam(teamDTO); }


}
