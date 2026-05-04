package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.team.TeamDTO;
import ulb.communication.SocketClient;
import ulb.communication.GameMode;
import ulb.exceptions.CommunicationException;
import ulb.message.ClientToServerMessage;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.message.serverToClient.NextWindowMessage.WindowType;
import ulb.model.battle.BattleState;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.model.chat.ChatMessage;
import ulb.exceptions.LoadException;

/**
 * Client-side application controller coordinating UI flow and server messaging.
 */
public class ClientController extends Application implements RegisterController.Listener, ModeController.Listener,
BattleModeController.Listener,BattleEndController.Listener, BattleWindowController.Listener, NextRoomController.Listener, 
FloorRewardController.Listener, AttackReplacementController.Listener, LevelUpController.Listener,
LoadTeamPanelController.Listener, FloorController.Listener {

	SocketClient client;
	private final Object serverRequestLock = new Object();
    Stage stage;

	PlayerDTO player;
	GameMode gameMode;

	RegisterController registerController;
    ModeController modeController;
	TeamController teamController;
	BattleModeController battleModeController;
	BattleWindowController battleWindowController;

	BattleEndController battleEndController;
	LevelUpController levelUpController;
	NextRoomController nextRoomController;
	FloorRewardController floorRewardController;
	ChooseBugemonController chooseBugemonController;
	AttackReplacementController attackReplacementController;

	FloorRewardController.RewardChoice pendingFloorRewardChoice;
	FloorController floorController;
	BugemonDTO pendingLevelUpBugemon;
	List<RewardDTO> pendingLevelUpRewards;

	SocialPanelController socialPanelController;
	WaitWindowController waitWindowController;

	public Stage getStage() { return this.stage; }

    /**
     * Initializes network client from application launch parameters.
     */
    @Override
    public void init(){
        List<String> params = getParameters().getRaw();

        String serverIp = params.get(0);
        Integer serverPort = Integer.parseInt(params.get(1));
        
        this.client = new SocketClient(serverIp, serverPort);
    }

    /**
     * Initializes the main stage and displays the register screen.
     *
     * @param primaryStage The JavaFX primary stage
     * @throws Exception If UI initialization fails
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
		
		this.stage = primaryStage;

		InputStream font = getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf");

        Font.loadFont(font, 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");

		this.registerController = new RegisterController(this.stage, this);
		this.registerController.show();

		if (primaryStage.getScene() != null) {
			String stylesheet = getClass().getResource("/styles/global.css").toExternalForm();
			if (!primaryStage.getScene().getStylesheets().contains(stylesheet)) {
				primaryStage.getScene().getStylesheets().add(stylesheet);
			}
		}

		primaryStage.show();
    }

	/**
	 * Send data to the server and return a boolean depending on the StatusMessage received
	 * @param message The message sent to the server
	 * @return A boolean that tells if the request has been accepted
	 */
	public boolean postData(ClientToServerMessage message){
		synchronized (serverRequestLock) {
			client.sendMessage(message);
			Serializable response = client.receiveMessage();

			if (response instanceof StatusMessage statusMessage) {
				if (statusMessage.isFailure()) {
					System.err.println(statusMessage.getMessage());
					return false;
				}
				return true;
			}

			System.err.println("Réponse inattendue du serveur : " + response);
			return false;
		}
	}

	/**
	 * Get data from the server
	 * @param message The messenge sent to the server
	 * @return The message received from the server and containing the data
	 */
	public Serializable getData(ClientToServerMessage message){
		synchronized (serverRequestLock) {
			client.sendMessage(message);
			return client.receiveMessage();
		}
	}

	/**
	 * Returns the currently authenticated player.
	 *
	 * @return Current player DTO
	 */
	public PlayerDTO getPlayer() {
		return this.player;
	}

	/**
	 * Retrieves a player DTO by username from server.
	 *
	 * @param username Username to retrieve
	 * @return Matching player DTO or null if unavailable
	 */
	public PlayerDTO getPlayer(String username) { 
		if (getData(new GetPlayerMessage(username)) instanceof PlayerMessage msg) {
			return msg.getPlayer();
		}
		return null; 
	}

	/**
	 * Sends a sign-up request for a player.
	 *
	 * @param player Player registration DTO
	 * @return True if account creation succeeded
	 */
	public boolean signUp(PlayerRegisterDTO player){
		return postData(new RegisterMessage(player, false));
	}

	/**
	 * Sends a login request for a player.
	 *
	 * @param player Player credentials DTO
	 * @return True if accepted by server
	 */
	public boolean logIn(PlayerRegisterDTO player){
		return postData(new RegisterMessage(player, true));
	}

	// Social Panel Controller

	public boolean sendBattleRequest(String receiver) {
		return postData(new SendBattleRequestMessage(player.getUsername(), receiver));
	}

	public List<String> getBattleRequests() {
		if (getData(new GetBattleRequestsMessage(player.getUsername())) instanceof BattleRequestsMessage msg)
			return msg.getRequests();
		return List.of();
	}

	public boolean acceptBattleRequest(String sender) {
		return postData(new AcceptBattleRequestMessage(player.getUsername(), sender));
	}

	public boolean declineBattleRequest(String sender) {
		return postData(new DeclineBattleRequestMessage(player.getUsername(), sender));
	}

	public MultiBattleStatusDTO getMultiBattleStatus(int userId1, int userId2) {
		if (getData(new GetMultiBattleStatusMessage(userId1, userId2)) instanceof MultiBattleStatusMessage msg)
			return msg.getStatus();

		throw new CommunicationException("Failed to obtain multiplayer battle status.");
	}

	public boolean sendFriendRequest(String receiver) {
		return postData(new SendFriendRequestMessage(player.getUsername(), receiver));
	}

	public List<String> getFriendRequests() {
		if (getData(new GetFriendRequestsMessage(player.getUsername())) instanceof FriendRequestsMessage msg)
			return msg.getRequests();
		return List.of();
	}

	public String getPlayerName() {
		return player.getUsername();
	}

	public boolean acceptFriendRequest(String sender) {
		return postData(new AcceptFriendRequestMessage(player.getUsername(), sender));
	}

	public boolean declineFriendRequest(String sender) {
		return postData(new DeclineFriendRequestMessage(player.getUsername(), sender));
	}

	public void sendChatMessage(String receiver, String content) {
		postData(new SendChatMessageMessage(player.getUsername(), receiver, content));
	}

	public List<ChatMessage> getChatMessages(String friend) {
		if (getData(new GetChatMessagesMessage(player.getUsername(), friend)) instanceof ChatMessagesMessage msg)
			return msg.getMessages();
		return List.of();
	}

	public List<String> getFriendsList() {
		if (getData(new GetFriendsListMessage(player.getUsername())) instanceof FriendsListMessage msg)
			return msg.getFriends();
		return List.of();
	}

	public Map<String, Integer> getLeaderboardList() {
		if (getData(new GetLeaderboardMessage()) instanceof LeaderboardMessage msg)
			return msg.getLeaderboard();
		return Collections.<String, Integer>emptyMap();
	}

	/**
	 * Switches to the team selection window for a multiplayer battle.
	 *
	 * @param opponent The opponent for the battle
	 */
	public void switchToTeamSelectionForMulti(PlayerDTO opponent) {
		this.teamController = new TeamController(this);
		this.teamController.setOpponent(opponent);

		this.teamController.show();
	}

	// Register Controller :

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLogin(String userName, String password){
		try {
			PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
			boolean success = logIn(playerDTO);
			if (success) {
				this.player = getPlayer(userName);
				if (this.player == null) {
					throw new RuntimeException("Player is null after login");
				}
				this.modeController = new ModeController(this.stage, this);
				try {
					this.modeController.show();
				}catch (Exception e){
					e.printStackTrace();
				}
			} else {
				this.registerController.getView().setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
			}
		} catch (LoadException e) {
			this.registerController.getView().setErrorLabel("Erreur de connexion à la base de données.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSignUp(String userName, String password){
		try {
			PlayerRegisterDTO playerDTO = new PlayerRegisterDTO(userName, password);
			boolean success = this.signUp(playerDTO);
			if (success) {
				this.player = getPlayer(userName);
				if (this.player == null) {
					throw new RuntimeException("Player is null after login");
				}
				this.modeController = new ModeController(this.stage, this);
				try {
					this.modeController.show();
				}catch (Exception e){
					e.printStackTrace();
				}
			} else {
				this.registerController.getView().setErrorLabel("Ce nom d'utilisateur est déjà pris.");
			}
		} catch (LoadException e) {
			this.registerController.getView().setErrorLabel("Erreur de connexion à la base de données.");
		}
	}

	// Mode Controller Listener :

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOpenSocial() {
		this.socialPanelController = new SocialPanelController(this);
		this.socialPanelController.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSolo() {
		this.teamController = new TeamController(this);
		this.teamController.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onMultiplayer() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onLogOut() {
		this.player = null;
		this.registerController.show();
	}

	// Team Controller :

	/**
	 * Returns the list of all the Bugemon species.
	 *
	 * @return A list of BugemonSpeciesDTO with all the species in the game's data
	 */
	public List<BugemonSpeciesDTO> getAllSpecies(){
		Serializable message = this.getData(new GetAllBugemonSpeciesMessage());

		if (message instanceof BugemonSpeciesMessage speciesMessage){
			return speciesMessage.getSpecies();
		}
		return null;
	}

    /**
     * Sends the player's team to the server and switches to the battle mode window
     */
	public void setupTeamAndShowModeMenu() {
		List<BugemonDTO> team = player.getTeam();

		if (!this.postData(new SetUpTeamMessage(team))){
			return;
		}

		this.battleModeController = new BattleModeController(this.stage, this, team);
		battleModeController.show();
	}

	/**
	 * Confirms the team for a multiplayer battle and starts waiting for the battle to start.
	 *
	 * @param opponent The view's player's opponent
	 */
	public void confirmTeamMulti(PlayerDTO opponent) {
		this.postData(new ConfirmTeamMultiMessage(opponent, this.getPlayer().getTeam()));

		this.openWaitWindow(e -> {
			this.waitForOpponentTeam(opponent);
		});
	}

	/**
	 * Function called in a loop while waiting for the opponent to pick his team.
	 *
	 * @param opponent This view's player's opponent
	 */
	private void waitForOpponentTeam(PlayerDTO opponent) {
		PlayerDTO self = this.getPlayer();
		MultiBattleStatusDTO status = this.getMultiBattleStatus(self.getUserId(), opponent.getUserId());

		switch(status.getStatus()) {
			case BATTLE:
				this.stopWaitWindow();
				this.startMultiBattle(opponent);
				break;

			case PICKING_TEAMS:
				break;

			default:
				this.stopWaitWindow();
				this.switchToModeWindow();
		}
	}

	/**
	 * Starts a multiplayer battle. To be called once both teams have been picked.
	 *
	 * @param opponent The opponent
	 */
	private void startMultiBattle(PlayerDTO opponent) {
		List<BugemonDTO> team = player.getTeam();
		this.postData(new SetUpTeamMessage(team));

		this.postData(new StartMultiBattleMessage(opponent));
		this.switchToBattleWindow();
	}

	/**
	 * Shows the Load team panel when the load a team button is clicked in create team window
	 */
	public void loadTeam() {
		LoadTeamPanelController loadTeamPanelController = new LoadTeamPanelController(stage, this);
		loadTeamPanelController.show();
	}

	/**
	 * Returns the player's saved teams from the database
	 *
	 * @return the player's saved teams
	 */
	@Override
	public List<TeamDTO> getSavedTeams() {
		Serializable message = this.getData(new GetSavedTeamsMessage());

		if (message instanceof SavedTeamsMessage teamsMessage){
			return teamsMessage.getTeams();
		}
		return null;
	}

	/**
	 * Saves the team to the database
	 * @param teamDTO the DTO of the team to be saved
	 */
	public void saveTeam(TeamDTO teamDTO) {
		boolean success = postData(new SaveTeamMessage(teamDTO));
		if (!success) {
			teamController.getView().showInvalidSaveAlert("Tu as déjà une équipe avec ce nom!");
		}
	}

	/**
	 * Loads the selected team from the load team panel
	 * @param selectedTeam the selected team
	 */
	@Override
	public void onTeamLoaded(TeamDTO selectedTeam) {
		player.setTeam(selectedTeam.members());
		setupTeamAndShowModeMenu();
	}

	// BattleEndController

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onHandleReturn() {
		switchToModeWindow();
	}

	// Battle Mode Controller Listener : 

	/**
	 * Shows the mode window.
	 */
	public void switchToModeWindow(){
		this.modeController.show();
	}

	/**
	 * Shows the next room window.
	 */
	private void switchToNextRoomWindow(){
		this.nextRoomController = new NextRoomController(stage, this);
		this.nextRoomController.show();
	}

	/**
	 * Shows the battle window using current mode context.
	 */
	private void switchToBattleWindow() {
		int towerFloorNumber = 0, towerRoomNumber = 0;
		if (this.gameMode == GameMode.TOWER){
			List<Integer> towerInfo;
			if ((towerInfo = this.getTowerInfo()) != null){
				towerFloorNumber = towerInfo.get(0);
				towerRoomNumber = towerInfo.get(1);
			}
		}
		battleWindowController = new BattleWindowController(
				this.stage,
				this,
				player,
				gameMode,
				towerFloorNumber,
				towerRoomNumber
		);

		battleWindowController.show();
	}

	private void switchToFloorWindow(){
		if (this.floorController == null) {
			this.floorController = new FloorController(this.stage, this);
		}

		this.floorController.show();
	}

	/**
	 * Shows the battle end window with current result payload.
	 */
	private void switchToBattleEndWindow(){
		Serializable message = getData(new GetBattleEndInfoMessage());
		boolean victory = false;
		int totalXp = 0;
		if (message instanceof BattleEndInfoMessage battleInfo){
			victory = battleInfo.isVictory();
			totalXp = battleInfo.getTotalXp();
		} else {
			return;
		}

		this.battleEndController = new BattleEndController(stage, this);
		battleEndController.show(victory, totalXp);
	}

	/**
	 * Shows the level-up window.
	 */
	private void switchToLevelUpWindow(){
		Serializable message = getData(new GetLevelUpInfoMessage());
		if (!(message instanceof LevelUpInfoMessage levelUpInfo)) {
			return;
		}

		this.pendingLevelUpBugemon = levelUpInfo.getBugemon();
		this.pendingLevelUpRewards = levelUpInfo.getRewards();
		this.levelUpController = new LevelUpController(stage, this);
		this.levelUpController.show();
	}

	/**
	 * Shows the tower reward window.
	 */
	private void switchToTowerRewardWindow(){
		this.floorRewardController = new FloorRewardController(stage, this);
		floorRewardController.show();
	}

	/**
	 * Routes to the next window according to server-provided flow state.
	 */
	public void nextRoom(){
		WindowType nextWindow = this.getWindowType();
		switch (nextWindow) {
			case NEXT_ROOM:
				this.floorController = null;
				switchToNextRoomWindow();
				break;

			case GAME:
				switchToBattleWindow();
				break;
			
			case LEVEL_UP:
				switchToLevelUpWindow();
				break;
			
			case REWARD:
				switchToTowerRewardWindow();
				break;
			
			case MAIN_MENU:
				switchToBattleEndWindow();
				break;

			case FLOOR:
				switchToFloorWindow();
				break;

			default:
				break;
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAutoBattle() {
		this.gameMode = GameMode.AUTO;
		if (this.postData(new SetUpNormalModeMessage())){
			switchToBattleWindow();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onControlledBattle() {
		this.gameMode = GameMode.CONTROLLED;
		if (this.postData(new SetUpNormalModeMessage())){
			switchToBattleWindow();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTowerMode(boolean newTower) {
		this.gameMode = GameMode.TOWER;
		this.floorController = null;
		if (this.postData(new SetUpTowerModeMessage(newTower))){
			switchToFloorWindow();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturnToCreateTeamWindow() {
		try {
			teamController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Battle Window Controller Listener : 

	/**
	 * Updates the inventory of the player.
	 * @param userName the userName of the player used to confirm the player identity on the server side
	 */
	@Override
	public void updatePlayerInventory(String userName){
		Serializable message = getData(new GetPlayerInventoryMessage(userName));
		if (message instanceof PlayerInventoryMessage playerInventory){
			this.player.setInventory(playerInventory.getInventory());
			
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBattleStateChecked(BattleState state, ActionEvent event) {
		if (state != BattleState.WON && state != BattleState.LOST){
			return;
		}
		nextRoom();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BugemonDTO> getActiveBugemons(){
		Serializable message = getData(new GetActiveBugemonsMessage());
		if (message instanceof ActiveBugemonsMessage activeBugemons){
			return List.of(activeBugemons.getSelfActiveBugemon(), activeBugemons.getOpponentActiveBugemon());
			
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BugemonDTO> getPlayerTeam(){
		Serializable message = getData(new GetPlayerTeamMessage());
		if (message instanceof PlayerTeamMessage playerTeam){
			return playerTeam.getBugemons();

		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
		Serializable message = getData(new GetAbilityEffectivenessMessage(abilities, bugemonTarget));

		if (message instanceof AbilityEffectivenessMessage effectivenessMessage){
			return effectivenessMessage.getEffectiveness();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getHpAfterFirstAction(){
		Serializable message = getData(new GetLogsMessage(false));

		if (message instanceof LogsMessage logs){
			return logs.getHpsAfterFirstAction();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BattleState getState(){
		Serializable message = getData(new GetBattleStateMessage());

		if (message instanceof BattleStateMessage battleState){
			return battleState.getBattleState();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getLogs(){
		Serializable message = getData(new GetLogsMessage(true));

		if (message instanceof LogsMessage logs){
			return logs.getLogs();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, Boolean> checkItems(List<ItemDTO> items){
		Serializable message = getData(new CheckUsableItemMessage(items));

		if (message instanceof UsableItemsMessage usableItems){
			return usableItems.getItemMap();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isGameFinished(){
		Serializable message = getData(new CheckGameFinishedMessage());

		if (message instanceof GameFinishedMessage gameFinished){
			return gameFinished.isGameFinished();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BattleState onAutoTurn() {
		if (!postData(new PickRandomActionMessage())){
			return null;
		}
		return getState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BattleState onUseItem(ItemDTO item) {
		if (!postData(new UseItemMessage(item))){
			return null;
		}
		return getState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BattleState onSwapBugemon(BugemonDTO bugemon) {
		if (!postData(new SwapBugemonMessage(bugemon))){
			return null;
		}
		return getState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BattleState onUseAbility(AbilityDTO ability) {
		if (!postData(new UseAbilityMessage(ability))){
			return null;
		}
		return getState();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRun() {
		if (postData(new RunMessage())){
			nextRoom();
		}
	}

	/**
	 * Returns the next window type according to server flow.
	 *
	 * @return The next window type, or null if unavailable
	 */
	public WindowType getWindowType(){
		Serializable message = getData(new GetNextWindowMessage());

		if (message instanceof NextWindowMessage nextWindow){
			return nextWindow.getNextWindow();
		}

		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<RewardDTO> getLevelUpRewards() {

		Serializable message = getData(new GetLevelUpInfoMessage());
		List<RewardDTO> rewards = null;

		if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
			
		} else if (message instanceof LevelUpInfoMessage levelUpInfo){
			rewards = levelUpInfo.getRewards();
		}
		return rewards;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRewardChosen(RewardDTO reward, ActionEvent event) {
		if (postData(new ChooseLevelUpRewardMessage(reward))) {
			nextRoom();
		}
	}

	// Next Room Listener 

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onContinue() {
		nextRoom();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturn() {
		try {
			if (this.postData(new AbandonTowerMessage())){
				this.modeController.show();
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	// Floor Reward Listener

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onObjectReward(ItemDTO rewardItem) {
		if (postData(new ChooseItemRewardMessage(rewardItem))){
			switchToFloorWindow();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onChooseBugemonReward(FloorRewardController.RewardChoice rewardChoice) {
		pendingFloorRewardChoice = rewardChoice;
		if (chooseBugemonController == null) {
			chooseBugemonController = new ChooseBugemonController(this.stage, this.floorRewardController, this.player);
		}
		try {
			chooseBugemonController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onBugemonChosen(BugemonDTO bugemon) {
		if (pendingFloorRewardChoice == FloorRewardController.RewardChoice.STAT) {
			if (postData(new ChooseStatRewardMessage(bugemon))){
				switchToFloorWindow();
			}
			return;
		}
		AbilityDTO newAbility = null;
		Serializable message = getData(new GetRandomAbilityMessage(bugemon));
		if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
			return; 

		}else if (message instanceof RandomAbilityMessage randomAbility){
			newAbility = randomAbility.getAbility();
		}

		if (newAbility == null) {
			nextRoom();
			return;
		}

		if (attackReplacementController == null) {
			attackReplacementController = new AttackReplacementController(stage, this);
		}
		try {
			attackReplacementController.show(bugemon, newAbility);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturnFloorRewardWindow() {
		if (floorRewardController == null) {
			floorRewardController = new FloorRewardController(stage, this);
		}
		try {
			floorRewardController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ItemDTO getRandomItem() {
		if (getData(new GetRandomItemMessage()) instanceof RandomItemMessage randomItem){
			return randomItem.getItem();
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Integer> getTowerInfo() {
		if (getData(new GetTowerInfoMessage()) instanceof TowerInfoMessage towerInfo){
			return List.of(towerInfo.getFloorNumber(), towerInfo.getRoomNumber());
		}
		return null;
	}

    /**
     * @return the list of the ids of the cleared rooms in the current floor
     */
	@Override
	public List<Integer> getClearedRooms() {
		if (getData(new GetTowerInfoMessage()) instanceof TowerInfoMessage towerInfo){
			return towerInfo.getClearedRooms();
		}
		return List.of();
	}

	// Attack Replacement Controller Listener

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAttackReplaced(BugemonDTO bugemon, AbilityDTO newAbility, AbilityDTO oldAbility) {
		if (postData(new ChooseAbilityRewardMessage(bugemon, oldAbility, newAbility))){
			switchToFloorWindow();
		}
	}

    /**
	 * {@inheritDoc}
	 */
    @Override
	public void onReturnToChooseBugemon() {
		if (chooseBugemonController == null) {
			return;
		}
		try {
			chooseBugemonController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// FloorController

	@Override
	public boolean onRoomSelected(int roomId) {
        return postData(new ChooseTowerRoomMessage(roomId));
    }

	@Override
	public void onRoomSelectionComplete() {
		nextRoom();
	}

	@Override
    public void onReturnFloorWindow() {
        try {
			this.battleModeController.show();
		}catch (Exception e){
			e.printStackTrace();
		}
    }

    // Miscellaneous

	/**
	 * Opens a waiting window.
	 *
	 * @param waitCycle The function to play in loop in the waiting window
	 */
    public void openWaitWindow(EventHandler waitCycle) {
		this.closeSocialPanel();

		this.waitWindowController = new WaitWindowController(this, waitCycle);
		this.waitWindowController.show();
    }

	/**
	 * Stops the main loop of a waiting window. To call before closing it.
	 */
	public void stopWaitWindow() {
		this.waitWindowController.stop();
	}

	/**
	 * Closes the social panel.
	 */
	public void closeSocialPanel() {
		this.socialPanelController.close();
	}
}
