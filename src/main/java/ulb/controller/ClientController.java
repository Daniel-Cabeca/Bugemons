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
import java.util.logging.Level;
import java.util.logging.Logger;

import ulb.DTO.battle.MultiBattleStatusDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.team.TeamDTO;
import ulb.communication.SocketClient;
import ulb.exceptions.CommunicationException;
import ulb.exceptions.ViewLoadException;
import ulb.communication.GameMode;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.message.clientToServer.gameActions.*;
import ulb.message.clientToServer.gameData.*;
import ulb.message.clientToServer.gameInfo.*;
import ulb.message.clientToServer.playerInfo.*;
import ulb.message.clientToServer.setup.*;
import ulb.message.clientToServer.social.*;
import ulb.message.clientToServer.teamSave.*;
import ulb.message.serverToClient.gameData.*;
import ulb.message.serverToClient.gameInfo.*;
import ulb.message.serverToClient.playerInfo.*;
import ulb.message.serverToClient.social.*;
import ulb.message.serverToClient.teamSave.*;
import ulb.model.battle.BattleState;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.player.PlayerRegisterDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.reward.RewardDTO;
import ulb.model.chat.ChatMessage;
import ulb.controller.windows.RegisterController;
import ulb.view.WindowPath;

/**
 * Client-side application controller coordinating server messaging.
 */
public class ClientController extends Application implements RegisterController.Listener, ModeController.Listener, GameModeController.Listener,
		ConfirmTeamController.Listener,BattleEndController.Listener, BattleWindowController.Listener, NextRoomController.Listener,
		FloorRewardController.Listener, AttackReplacementController.Listener, LevelUpController.Listener,
		LoadTeamPanelController.Listener, FloorController.Listener {

	private static final Logger LOGGER = Logger.getLogger(ClientController.class.getName());

	SocketClient client;
	private final Object serverRequestLock = new Object();
	Stage stage;

	PlayerDTO player;
	GameMode gameMode;

	RegisterController registerController;
	ModeController modeController;
	GameModeController gameModeController;
	TeamController teamController;
	ConfirmTeamController confirmTeamController;
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

	/**
	 * Returns the application stage.
	 *
	 * @return The application stage
	 */
	public Stage getStage() { return this.stage; }

	private void logViewLoadFailure(String errorMessage, ViewLoadException e) {
		LOGGER.log(Level.WARNING, errorMessage, e);
	}

    /**
     * Initializes network client from application launch parameters.
     */
    @Override
    public void init() throws CommunicationException {
        List<String> params = getParameters().getRaw();

		String serverIp = params.get(0);
		Integer serverPort = Integer.parseInt(params.get(1));

		this.client = new SocketClient(serverIp, serverPort);
	}

	/**
	 * Initializes the main stage and displays the register screen.
	 *
	 * @param primaryStage The primary stage
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

		this.registerController = new RegisterController(this.stage, WindowPath.REGISTER,this);
		this.registerController.show();

		this.modeController = new ModeController(this.stage, this);

		if (primaryStage.getScene() != null) {
			String stylesheet = getClass().getResource("/styles/global.css").toExternalForm();
			if (!primaryStage.getScene().getStylesheets().contains(stylesheet)) {
				primaryStage.getScene().getStylesheets().add(stylesheet);
			}
		}

		primaryStage.show();
	}

	/**
	 * Sends data to the server and returns whether the request was accepted.
	 *
	 * @param message The message sent to the server
	 * @return True if the request was accepted by the server
	 */
	public boolean postData(ClientToServerMessage message){
		synchronized (serverRequestLock) {
			try {
				client.sendMessage(message);
				Serializable response = client.receiveMessage();
				if (response instanceof StatusMessage statusMessage) {
					if (statusMessage.isFailure()) {
						return false;
					}
					return true;
				}
			} catch (CommunicationException e) {
				LOGGER.log(Level.WARNING, "Communication error with server.", e);
			}
			return false;
		}
	}

	/**
	 * Sends a request to the server and returns the response.
	 *
	 * @param message The message sent to the server
	 * @return The response received from the server
	 */
	public Serializable getData(ClientToServerMessage message){
		synchronized (serverRequestLock) {
			try {
				client.sendMessage(message);
				return client.receiveMessage();
			} catch (CommunicationException e) {
				return new StatusMessage(false, "Connexion perdue avec le serveur.");
			}
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
	 * Sends a login request
	 *
	 * @param player Player credentials DTO
	 * @return True if accepted by server
	 */
	public boolean logIn(PlayerRegisterDTO player){
		return postData(new RegisterMessage(player, true));
	}

	// Social Panel Controller

	/**
	 * Sends a battle request to the chosen player
	 *
	 * @param receiver The username of the player to challenge
	 * @return True if the request was accepted by the server
	 */
	public boolean sendBattleRequest(String receiver) {
		return postData(new SendBattleRequestMessage(player.getUsername(), receiver));
	}

	/**
	 * Returns the list of pending incoming battle requests.
	 *
	 * @return The list of player usernames who sent a battle request
	 */
	public List<String> getBattleRequests() {
		if (getData(new GetBattleRequestsMessage(player.getUsername())) instanceof BattleRequestsMessage msg)
			return msg.getRequests();
		return List.of();
	}

	/**
	 * Accepts a battle request from the given player.
	 *
	 * @param sender The username of the player who sent the request
	 * @return True if the acceptance was accepted by the server
	 */
	public boolean acceptBattleRequest(String sender) {
		return postData(new AcceptBattleRequestMessage(player.getUsername(), sender));
	}

	/**
	 * Declines a battle request from the given player.
	 *
	 * @param sender The username of the player who sent the request
	 * @return True if the decline was accepted by the server
	 */
	public boolean declineBattleRequest(String sender) {
		return postData(new DeclineBattleRequestMessage(player.getUsername(), sender));
	}

	/**
	 * Returns the current multiplayer battle status between two players.
	 *
	 * @param userId1 The first player's id
	 * @param userId2 The second player's id
	 * @return The multiplayer battle status DTO
	 */
	public MultiBattleStatusDTO getMultiBattleStatus(int userId1, int userId2) {
		if (getData(new GetMultiBattleStatusMessage(userId1, userId2)) instanceof MultiBattleStatusMessage msg)
			return msg.getStatus();

		LOGGER.warning("Failed to obtain multiplayer battle status.");
		return new MultiBattleStatusDTO();
	}

	/**
	 * Sends a friend request to the given player.
	 *
	 * @param receiver The username of the player to add as a friend
	 * @return True if the request was acknowledged by the server
	 */
	public boolean sendFriendRequest(String receiver) {
		return postData(new SendFriendRequestMessage(player.getUsername(), receiver));
	}

	/**
	 * Returns the list of pending friend requests.
	 *
	 * @return The list of usernames who sent a friend request
	 */
	public List<String> getFriendRequests() {
		if (getData(new GetFriendRequestsMessage(player.getUsername())) instanceof FriendRequestsMessage msg)
			return msg.getRequests();
		return List.of();
	}

	/**
	 * Returns the current player's username.
	 *
	 * @return The player's username
	 */
	public String getPlayerName() {
		return player.getUsername();
	}

	/**
	 * Accepts a friend request from the given player.
	 *
	 * @param sender The username of the player who sent the request
	 * @return True if the acceptance was acknowledged by the server
	 */
	public boolean acceptFriendRequest(String sender) {
		return postData(new AcceptFriendRequestMessage(player.getUsername(), sender));
	}

	/**
	 * Declines a friend request from the given player.
	 *
	 * @param sender The username of the player who sent the request
	 * @return True if the decline was acknowledged by the server
	 */
	public boolean declineFriendRequest(String sender) {
		return postData(new DeclineFriendRequestMessage(player.getUsername(), sender));
	}

	/**
	 * Sends a chat message to the given player.
	 *
	 * @param receiver The username of the message recipient
	 * @param content The content of the message
	 */
	public void sendChatMessage(String receiver, String content) {
		postData(new SendChatMessageMessage(player.getUsername(), receiver, content));
	}

	/**
	 * Returns the chat message history with the given friend.
	 *
	 * @param friend The username of the friend
	 * @return The list of chat messages exchanged with that friend
	 */
	public List<ChatMessage> getChatMessages(String friend) {
		if (getData(new GetChatMessagesMessage(player.getUsername(), friend)) instanceof ChatMessagesMessage msg)
			return msg.getMessages();
		return List.of();
	}

	/**
	 * Returns the current player's friends list.
	 *
	 * @return The friends list
	 */
	public List<String> getFriendsList() {
		if (getData(new GetFriendsListMessage(player.getUsername())) instanceof FriendsListMessage msg)
			return msg.getFriends();
		return List.of();
	}

	/**
	 * Returns the leaderboard of the best players.
	 *
	 * @return The leaderboard map, or an empty map if unavailable
	 */
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

		try {
			this.teamController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de sélection d'équipe.", e);
		}
	}

	// Register Controller :

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onSignUp(PlayerRegisterDTO playerDTO) {
		return this.signUp(playerDTO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onLogin(PlayerRegisterDTO playerRegisterDTO) {
		return this.logIn(playerRegisterDTO);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void showModeWindow() throws ViewLoadException {
		this.modeController.show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerDTO onGetPlayer(String userName) {
		this.player = this.getPlayer(userName);
		return this.player;
	}

	// Mode Controller Listener :

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onOpenSocial() {
		this.socialPanelController = new SocialPanelController(this);
		try {
			this.socialPanelController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher le panneau social.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onSolo() {
		switchToGameModeWindow();
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

	// Game Mode Controller :

	/**
	 * Switches to the create team window.
	 */
	private void switchToCreateTeamWindow() {
		this.teamController = new TeamController(this);
		try {
			this.teamController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de sélection d'équipe.", e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onAutoBattle() {
		this.gameMode = GameMode.AUTO;
		switchToCreateTeamWindow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onControlledBattle() {
		this.gameMode = GameMode.CONTROLLED;
		switchToCreateTeamWindow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTowerMode(boolean newTower) {
		this.gameMode = GameMode.TOWER;
		this.floorController = null;
		if (newTower) {
			switchToCreateTeamWindow();
		} else {
			if (this.postData(new SetUpTowerModeMessage(newTower))){
				switchToFloorWindow();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTowerSaved() {
		Serializable message = getData(new GetTowerSavedInfoMessage());
		if (message instanceof TowerSavedInfoMessage towerInfoMessage){
			return towerInfoMessage.isTowerSaved();
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturnToModeWindow() {
		switchToModeWindow();
	}

	// Team Controller :

	/**
	 * Returns the list of all the Bugemon species.
	 *
	 * @return A list of all the species of Bugemon
	 */
	public List<BugemonSpeciesDTO> getAllSpecies(){
		Serializable message = this.getData(new GetAllBugemonSpeciesMessage());

		if (message instanceof BugemonSpeciesMessage speciesMessage){
			return speciesMessage.getSpecies();
		}
		return null;
	}

	/**
	 * Sends the player's team to the server and switches to the battle mode window.
	 */
	public void setupTeamAndShowModeMenu() {
		List<BugemonDTO> team = player.getTeam();

		if (!this.postData(new SetUpTeamMessage(team))){
			return;
		}

		this.confirmTeamController = new ConfirmTeamController(this.stage, this, team, this.gameMode);
		try {
			confirmTeamController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de confirmation d'équipe.", e);
		}
	}

	/**
	 * Confirms the team for a multiplayer battle and waits for the battle to start.
	 *
	 * @param opponent The opponent
	 */
	public void confirmTeamMulti(PlayerDTO opponent) {
		this.postData(new ConfirmTeamMultiMessage(opponent, this.getPlayer().getTeam()));

		this.openWaitWindow(e -> {
			this.waitForOpponentTeam(opponent);
		});
	}

	/**
	 * Called in a loop while waiting for the opponent to pick his team.
	 *
	 * @param opponent The opponent
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
	 * Starts a battle once both teams have been picked.
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
	 * Shows the load team panel when the load a team button is clicked.
	 */
	public void loadTeam() {
		LoadTeamPanelController loadTeamPanelController = new LoadTeamPanelController(stage, this);
		try {
			loadTeamPanelController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher le panneau de chargement d'équipe.", e);
		}
	}

	/**
	 * Returns the player's saved teams from the database.
	 *
	 * @return The player's saved teams
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
	 * Saves the team to the database.
	 *
	 * @param teamDTO The DTO of the team to be saved
	 */
	public void saveTeam(TeamDTO teamDTO) {
		boolean success = postData(new SaveTeamMessage(teamDTO));
		if (!success) {
			teamController.getView().showInvalidSaveAlert("Tu as déjà une équipe avec ce nom!");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onTeamLoaded(TeamDTO selectedTeam) {
		player.setTeam(selectedTeam.members());
		setupTeamAndShowModeMenu();
	}

	// Confirm Team Controller

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConfirm() {
		switch (this.gameMode) {
			case AUTO, CONTROLLED :
				if (this.postData(new SetUpNormalModeMessage())){
					switchToBattleWindow();
				}
				break;
			case TOWER:
				if (this.postData(new SetUpTowerModeMessage(true))){
					switchToFloorWindow();
				}
				break;
			default:
				switchToModeWindow();
				break;
		}
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
		try {
			this.modeController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher le menu principal.", e);
		}
	}

	/**
	 * Switches to the game mode selection window.
	 */
	public void switchToGameModeWindow() {
		if (this.gameModeController == null) {
			this.gameModeController = new GameModeController(stage, this);
		}
		try {
			this.gameModeController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible de retourner à l'écran de mode de jeu.", e);
		}
	}

	/**
	 * Switches to the next room window.
	 */
	private void switchToNextRoomWindow(){
		this.nextRoomController = new NextRoomController(stage, this);
		try {
			this.nextRoomController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de salle suivante.", e);
		}
	}

	/**
	 * Switches to the battle window according to game mode (and tower floor and room number if Tower mode)
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

		try {
			battleWindowController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de combat.", e);
		}
	}

	/**
	 * Switches to the floor window and creates the controller if needed.
	 */
	private void switchToFloorWindow(){
		if (this.floorController == null) {
			this.floorController = new FloorController(this.stage, this);
		}

		try {
			this.floorController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'étage de la tour.", e);
		}
	}

	/**
	 * Switches to the battle end window with battle result.
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
		try {
			battleEndController.show(victory, totalXp);
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de fin de combat.", e);
		}
	}

	/**
	 * Switches to the level-up window.
	 */
	private void switchToLevelUpWindow(){
		Serializable message = getData(new GetLevelUpInfoMessage());
		if (!(message instanceof LevelUpInfoMessage levelUpInfo)) {
			return;
		}

		this.pendingLevelUpBugemon = levelUpInfo.getBugemon();
		this.pendingLevelUpRewards = levelUpInfo.getRewards();
		this.levelUpController = new LevelUpController(stage, this);
		try {
			this.levelUpController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de montée de niveau.", e);
		}
	}

	/**
	 * Switches to the tower reward window.
	 */
	private void switchToTowerRewardWindow(){
		this.floorRewardController = new FloorRewardController(stage, this);
		try {
			floorRewardController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de récompense d'étage.", e);
		}
	}

	/**
	 * Switches to the next window according to info gotten from the server.
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
	public void onReturnToCreateTeamWindow() {
		try {
			teamController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible de retourner à l'écran de constitution d'équipe.", e);
		}
	}

	// Battle Window Controller Listener :

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePlayerInventory(String userName){
		Serializable message = getData(new GetPlayerInventoryMessage(userName));
		if (message instanceof PlayerInventoryMessage playerInventory){
			this.player.setInventory(playerInventory.getInventory());
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to update player inventory: " + errorMessage.getMessage());
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
			LOGGER.log(Level.WARNING, "Failed to get active bugemons: " + errorMessage.getMessage());
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
			LOGGER.log(Level.WARNING, "Failed to get player team: " + errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public Map<AbilityDTO, String> getAbilityEffectiveness(List<AbilityDTO> abilities, BugemonDTO bugemonTarget){
		Serializable message = getData(new GetAbilityEffectivenessMessage(abilities, bugemonTarget));
		if (message instanceof AbilityEffectivenessMessage effectivenessMessage){
			return effectivenessMessage.getEffectiveness();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get ability effectiveness: " + errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public List<Integer> getHpAfterFirstAction(){
		Serializable message = getData(new GetLogsMessage(false));
		if (message instanceof LogsMessage logs){
			return logs.getHpsAfterFirstAction();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get HP after first action: " + errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public BattleState getState(){
		Serializable message = getData(new GetBattleStateMessage());
		if (message instanceof BattleStateMessage battleState){
			return battleState.getBattleState();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get battle state: " + errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public List<String> getLogs(){
		Serializable message = getData(new GetLogsMessage(true));
		if (message instanceof LogsMessage logs){
			return logs.getLogs();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to get logs: " + errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public Map<String, Boolean> checkItems(List<ItemDTO> items){
		Serializable message = getData(new CheckUsableItemMessage(items));
		if (message instanceof UsableItemsMessage usableItems){
			return usableItems.getItemMap();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to check usable items: " + errorMessage.getMessage());
		}
		return null;
	}

	@Override
	public boolean isGameFinished(){
		Serializable message = getData(new CheckGameFinishedMessage());
		if (message instanceof GameFinishedMessage gameFinished){
			return gameFinished.isGameFinished();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			LOGGER.log(Level.WARNING, "Failed to check if game is finished: " + errorMessage.getMessage());
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BattleState onAutoTurn() {
		if (!postData(new ChooseRandomActionMessage())){
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
			LOGGER.log(Level.WARNING, "Failed to get level up rewards: " + errorMessage.getMessage());
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
		if (this.postData(new AbandonTowerMessage())){
			try {
				this.modeController.show();
			} catch (ViewLoadException e) {
				logViewLoadFailure("Impossible de retourner au menu après abandon de la tour.", e);
			}
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
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de choix du Bugémon pour la récompense.", e);
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
			LOGGER.log(Level.WARNING, "Failed to get random ability: " + errorMessage.getMessage());
			return;

		} else if (message instanceof RandomAbilityMessage randomAbility){
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
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de remplacement d'attaque.", e);
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
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de récompense d'étage.", e);
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
	 * Returns the list of ids of the cleared rooms in the current floor.
	 *
	 * @return The list of cleared room ids
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
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible de retourner à l'écran de choix du Bugémon.", e);
		}
	}

	// FloorController

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onRoomSelected(int roomId) {
		return postData(new ChooseTowerRoomMessage(roomId));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRoomSelectionComplete() {
		nextRoom();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onReturnToGameModeWindow() {
		switchToGameModeWindow();
	}

	// Miscellaneous

	/**
	 * Opens a waiting window.
	 *
	 * @param waitCycle The event handler to play in a loop
	 */
	public void openWaitWindow(EventHandler waitCycle) {
		this.closeSocialPanel();

		this.waitWindowController = new WaitWindowController(this, waitCycle);
		try {
			this.waitWindowController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran d'attente.", e);
		}
    }

	/**
	 * Stops the main loop of the waiting window.
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