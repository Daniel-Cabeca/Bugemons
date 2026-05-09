package ulb.server;

import ulb.communication.Messenger.SocketMessenger;
import ulb.exceptions.CommunicationException;
import ulb.message.ClientToServerMessage;
import ulb.exceptions.DataAccessException;
import ulb.exceptions.UserFacingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ulb.message.clientToServer.*;
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

public class ClientHandler extends Thread implements ServerMessageHandler{
    private SocketMessenger socketMessenger;
    private boolean stop;
	private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());

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
			sendErrorMessage("A data access error occurred while handling a client message.");
		} catch (RuntimeException e) {
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

	@Override public void handle(ConfirmTeamMultiMessage message) throws DataAccessException { setupHandler.handle(message); }
	@Override public void handle(RegisterMessage message) throws DataAccessException { setupHandler.handle(message); }
	@Override public void handle(SetUpNormalModeMessage message) throws DataAccessException { setupHandler.handle(message); }
	@Override public void handle(SetUpTeamMessage message) throws DataAccessException { setupHandler.handle(message); }
	@Override public void handle(SetUpTowerModeMessage message) throws DataAccessException { setupHandler.handle(message); }


	// PLAYER INFO

	@Override public void handle(GetPlayerMessage message) throws DataAccessException { playerInfoHandler.handle(message); }
	@Override public void handle(GetPlayerInventoryMessage message) throws DataAccessException { playerInfoHandler.handle(message); }
	@Override public void handle(GetPlayerTeamMessage message) throws DataAccessException { playerInfoHandler.handle(message); }
	@Override public void handle(GetUserIdFromNameMessage message) throws DataAccessException { playerInfoHandler.handle(message); }


	// GAME INFO

	@Override public void handle(CheckGameFinishedMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(CheckUsableItemMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetAbilityEffectivenessMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetActiveBugemonsMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetBattleEndInfoMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetBattleStateMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetLevelUpInfoMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetLogsMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetNextWindowMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetTowerInfoMessage message) throws DataAccessException { gameInfoHandler.handle(message); }
	@Override public void handle(GetTowerSavedInfoMessage message) throws DataAccessException { gameInfoHandler.handle(message, this.player); }

	// GAME ACTIONS

	@Override public void handle(AbandonTowerMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseAbilityRewardMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseItemRewardMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseLevelUpRewardMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseStatRewardMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseTowerRoomMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(PickRandomActionMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(RunMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(StartMultiBattleMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(SwapBugemonMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(UseAbilityMessage message) throws DataAccessException { gameActionsHandler.handle(message); }
	@Override public void handle(UseItemMessage message) throws DataAccessException { gameActionsHandler.handle(message); }


	// GAME DATA

	@Override public void handle(GetAllBugemonSpeciesMessage message) throws DataAccessException { gameDataHandler.handle(message); }
	@Override public void handle(GetRandomAbilityMessage message) throws DataAccessException { gameDataHandler.handle(message); }
	@Override public void handle(GetRandomItemMessage message) throws DataAccessException { gameDataHandler.handle(message); }


	// SOCIAL

	@Override public void handle(AcceptBattleRequestMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(AcceptFriendRequestMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(DeclineBattleRequestMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(DeclineFriendRequestMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(GetBattleRequestsMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(GetMultiBattleStatusMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(GetChatMessagesMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(GetFriendRequestsMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(GetFriendsListMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(SendBattleRequestMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(SendChatMessageMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(SendFriendRequestMessage message) throws DataAccessException { socialHandler.handle(message); }
	@Override public void handle(GetLeaderboardMessage message) throws DataAccessException {socialHandler.handle(message); }


	// TEAM SAVE

	@Override public void handle(GetSavedTeamsMessage message) throws DataAccessException { teamSaveHandler.handle(message); }
	@Override public void handle(SaveTeamMessage message) throws DataAccessException { teamSaveHandler.handle(message); }


}
