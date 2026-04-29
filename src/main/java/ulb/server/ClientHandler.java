package ulb.server;

import ulb.communication.Messenger.SocketMessenger;
import ulb.message.ClientToServerMessage;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.model.Player;
import ulb.model.battle.Battle;
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

    private Player player;

    private Battle battle;
    private Battle.ParticipantLabel teamLabel;
    private Thread opponentBot;

	private TowerManager towerManager;
	private boolean isGameTower;

	private Bugemon pendingLevelUpBugemon;
	private List<Reward> pendingLevelUpRewards;

	private final TowerSaveService towerSaveService;

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
                         AccountService accountService, ChatService chatService, TeamService teamService, InventoryService inventoryService, TowerSaveService towerSaveService) {
        this.socketMessenger = messenger;
		this.stop = false;
		this.towerSaveService = towerSaveService;

		this.setupHandler = new SetupHandler(this, accountService, itemService, inventoryService, bugemonService);
		this.gameInfoHandler = new GameInfoHandler(this);
		this.gameActionsHandler = new GameActionsHandler(this, inventoryService);
		this.socialHandler = new SocialHandler(this, accountService, chatService);
		this.playerInfoHandler = new PlayerInfoHandler(this, accountService);
		this.gameDataHandler = new GameDataHandler(this, bugemonService, abilityService, itemService);
		this.teamSaveHandler = new TeamSaveHandler(this, teamService);
    }

	public Player getPlayer() { return this.player; }
	public Battle getBattle() { return this.battle; }
	public TowerManager getTowerManager() { return this.towerManager; }
	public boolean isGameTower() { return this.isGameTower; }
	public Battle.ParticipantLabel getTeamLabel() { return this.teamLabel; }
	public Thread getOpponentBot() { return this.opponentBot; }
	public Bugemon getPendingLevelUpBugemon() { return this.pendingLevelUpBugemon; }
	public List<Reward> getPendingLevelUpRewards() { return this.pendingLevelUpRewards; }

	public void setPlayer(Player player) { this.player = player; }
	public void setTeam(Team team) { this.player.setTeam(team); }
	public void setBattle(Battle battle) { this.battle = battle; }
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
		
		message.dispatch(this);
    }

    public void stopProcess(){
        this.stop = true;
    }

    public ClientToServerMessage receiveMessage(){
        try{
			Serializable received = this.socketMessenger.receiveMessage();

			if (received instanceof ClientToServerMessage message) {
				return message;
			}

            return null;
        } catch (Exception e){
            stopProcess();
        }
        return null;
    }

    public void sendMessage(Serializable message){
        try{
            this.socketMessenger.sendMessage(message);
        } catch (Exception e){
            stopProcess();
        }
    }

    public void sendErrorMessage(String errorMessage){
        sendMessage(new StatusMessage(false, errorMessage));
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
	public void nextTowerRoom(){
		if (isGameTower){
			this.towerManager.nextRoom();
			this.battle = this.towerManager.getCurrentBattle();
			this.towerSaveService.saveTowerInfo(this.towerManager.getTower(), this.player);
		}
	}

	void finishTower(){
		if (!isGameTower){return;}
		this.towerSaveService.deleteTowerInfo(player);
		this.resetGameSessionState();
	}

	void clearPendingLevelUpState() {
		this.pendingLevelUpBugemon = null;
		this.pendingLevelUpRewards = null;
	}

	// SETUP

	@Override public void handle(RegisterMessage message) { setupHandler.handle(message); }
	@Override public void handle(SetUpNormalModeMessage message) { setupHandler.handle(message); }
	@Override public void handle(SetUpTeamMessage message) { setupHandler.handle(message); }
	@Override public void handle(SetUpTowerModeMessage message) { setupHandler.handle(message); }


	// PLAYER INFO

	@Override public void handle(GetPlayerMessage message) { playerInfoHandler.handle(message); }
	@Override public void handle(GetPlayerInventoryMessage message) { playerInfoHandler.handle(message); }
	@Override public void handle(GetPlayerTeamMessage message) { playerInfoHandler.handle(message); }
	@Override public void handle(GetUserIdFromNameMessage message) { playerInfoHandler.handle(message); }


	// GAME INFO

	@Override public void handle(CheckGameFinishedMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(CheckUsableItemMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetAbilityEffectivenessMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetActiveBugemonsMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetBattleEndInfoMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetBattleStateMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetLevelUpInfoMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetLogsMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetNextWindowMessage message) { gameInfoHandler.handle(message); }
	@Override public void handle(GetTowerInfoMessage message) { gameInfoHandler.handle(message); }


	// GAME ACTIONS

	@Override public void handle(AbandonTowerMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseAbilityRewardMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseItemRewardMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseLevelUpRewardMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(ChooseStatRewardMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(PickRandomActionMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(RunMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(SwapBugemonMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(UseAbilityMessage message) { gameActionsHandler.handle(message); }
	@Override public void handle(UseItemMessage message) { gameActionsHandler.handle(message); }


	// GAME DATA

	@Override public void handle(GetAllBugemonSpeciesMessage message) { gameDataHandler.handle(message); }
	@Override public void handle(GetRandomAbilityMessage message) { gameDataHandler.handle(message); }
	@Override public void handle(GetRandomItemMessage message) { gameDataHandler.handle(message); }


	// SOCIAL

	@Override public void handle(AcceptBattleRequestMessage message) { socialHandler.handle(message); }
	@Override public void handle(AcceptFriendRequestMessage message) { socialHandler.handle(message); }
	@Override public void handle(DeclineBattleRequestMessage message) { socialHandler.handle(message); }
	@Override public void handle(DeclineFriendRequestMessage message) { socialHandler.handle(message); }
	@Override public void handle(GetBattleRequestsMessage message) { socialHandler.handle(message); } 
	@Override public void handle(GetChatMessagesMessage message) { socialHandler.handle(message); }
	@Override public void handle(GetFriendRequestsMessage message) { socialHandler.handle(message); }
	@Override public void handle(GetFriendsListMessage message) { socialHandler.handle(message); }
	@Override public void handle(SendBattleRequestMessage message) { socialHandler.handle(message); }
	@Override public void handle(SendChatMessageMessage message) { socialHandler.handle(message); }
	@Override public void handle(SendFriendRequestMessage message) { socialHandler.handle(message); }


	// TEAM SAVE

	@Override public void handle(GetSavedTeamsMessage message) { teamSaveHandler.handle(message); }
	@Override public void handle(SaveTeamMessage message) { teamSaveHandler.handle(message); }

}
