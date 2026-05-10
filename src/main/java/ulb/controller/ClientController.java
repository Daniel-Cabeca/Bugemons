package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ulb.communication.SocketClient;
import ulb.controller.windows.*;
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
import ulb.message.serverToClient.gameData.*;
import ulb.message.serverToClient.gameInfo.*;
import ulb.message.serverToClient.playerInfo.*;
import ulb.model.battle.BattleState;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.item.ItemDTO;
import ulb.DTO.reward.RewardDTO;

/**
 * Client-side application controller coordinating server messaging.
 */
public class ClientController extends Application implements BattleWindowController.Listener, WindowController.ClientListener {

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
	LoadTeamPanelController loadTeamPanelController;

	RewardChoice pendingFloorRewardChoice;
	FloorController floorController;

	SocialPanelController socialPanelController;
	WaitWindowController waitWindowController;

	public Stage getStage() { return stage; }

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
		int serverPort = Integer.parseInt(params.get(1));

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

		//TODO: check where to put
		this.registerController = new RegisterController(this.stage, this);
		this.modeController = new ModeController(this.stage, this);
		this.socialPanelController = new SocialPanelController(this.stage, this);
		this.gameModeController = new GameModeController(this.stage, this);
		this.teamController = new TeamController(this.stage, this);
		this.floorController = new FloorController(this.stage, this);
		this.floorRewardController = new FloorRewardController(this.stage, this);
		this.levelUpController = new LevelUpController(this.stage, this);
		this.waitWindowController = new WaitWindowController(this.stage, this);
		this.confirmTeamController = new ConfirmTeamController(this.stage, this);
		this.loadTeamPanelController = new LoadTeamPanelController(this.stage, this);
		this.nextRoomController = new NextRoomController(this.stage, this);
		this.levelUpController = new LevelUpController(this.stage, this);
		this.floorRewardController = new FloorRewardController(this.stage, this);
		this.battleEndController = new BattleEndController(this.stage, this);
		this.chooseBugemonController = new ChooseBugemonController(this.stage, this);
		//TODO: implement battleWindow differently

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
	 * Sends data to the server and returns whether the request was accepted.
	 *
	 * @param message The message sent to the server
	 * @return True if the request was accepted by the server
	 */
	private boolean postData(ClientToServerMessage message){
		synchronized (serverRequestLock) {
			try {
				client.sendMessage(message);
				Serializable response = client.receiveMessage();
				if (response instanceof StatusMessage statusMessage) {
                    return !statusMessage.isFailure();
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
	private Serializable getData(ClientToServerMessage message){
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

	// Register Controller :

	/**
	 * Loads a player by username, stores it as the current player, and returns it.
	 *
	 * @param userName The username to look up
	 * @return The player DTO, or null if not found
	 */
	private PlayerDTO loadPlayer(String userName) {
		this.player = this.getPlayer(userName);
		return this.player;
	}

	// Mode Controller Listener :

	@Override
	public void onOpenSocial() {
		this.socialPanelController.show();
	}

	// Game Mode Controller :

	private void setGameMode(GameMode gameMode){this.gameMode = gameMode;}

	/**
	 * Switches to the next room window.
	 */
	private void switchToNextRoomWindow(){
		this.nextRoomController.show();
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
	private void switchToFloorWindow() {
		this.floorController.show();
	}

	/**
	 * Switches to the battle end window with battle result.
	 */
	private void switchToBattleEndWindow(){
		Serializable message = getData(new GetBattleEndInfoMessage());
		boolean victory;
		int totalXp;
		String opponent;
		boolean multiplayerBattle;
		if (message instanceof BattleEndInfoMessage battleInfo){
			victory = battleInfo.isVictory();
			totalXp = battleInfo.getTotalXp();
			multiplayerBattle = battleInfo.isMultiplayerBattle();
			if (multiplayerBattle)
				opponent = this.teamController.getOpponent().getUsername();
			else {
				opponent = "BotPlayer";
			}

		} else {
			return;
		}

			battleEndController.show(victory, totalXp, opponent, multiplayerBattle);
			this.teamController.resetOpponent();
	}

	/**
	 * Switches to the level-up window.
	 */
	private void switchToLevelUpWindow() {
		this.levelUpController.show();
	}

	/**
	 * Switches to the tower reward window.
	 */
	private void switchToTowerRewardWindow() {
		this.floorRewardController.show();
	}

	/**
	 * Switches to the next window according to info gotten from the server.
	 */
	public void nextRoom(){
		WindowType nextWindow = this.getWindowType();
		switch (nextWindow) {
			case NEXT_ROOM:
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
	public void onRewardChosen(RewardDTO reward, ActionEvent event) {
		if (postData(new ChooseLevelUpRewardMessage(reward))) {
			nextRoom();
		}
	}

	// Floor Reward Listener

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onChooseBugemonReward(RewardChoice rewardChoice) {
		pendingFloorRewardChoice = rewardChoice;
		if (chooseBugemonController == null) {
			chooseBugemonController = new ChooseBugemonController(this.stage, this);
		}
		try {
			chooseBugemonController.show();
		} catch (ViewLoadException e) {
			logViewLoadFailure("Impossible d'afficher l'écran de choix du Bugémon pour la récompense.", e);
		}
	}

	public void onBugemonChosen(BugemonDTO bugemon) {
		if (pendingFloorRewardChoice == RewardChoice.STAT) {
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
		attackReplacementController.show(bugemon, newAbility);
	}

	public void onReturnFloorRewardWindow() {
		this.floorRewardController.show();
	}

	/**
	 * Returns the current floor number and room number from the server.
	 */
	public List<Integer> getTowerInfo() {
		if (getData(new GetTowerInfoMessage()) instanceof TowerInfoMessage towerInfo){
			return List.of(towerInfo.getFloorNumber(), towerInfo.getRoomNumber());
		}
		return null;
	}

	// FloorController

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onRoomSelectionComplete() {
		nextRoom();
	}

	@Override
	public void onSetOpponentMulti(PlayerDTO opponent) {
		this.teamController.setOpponent(opponent);
	}


	@Override
	public Serializable onGetData(ClientToServerMessage message) {
		return this.getData(message);
	}

	@Override
	public boolean onPostData(ClientToServerMessage message) {
		return this.postData(message);
	}

	@Override
	public PlayerDTO onLoadPlayer(String userName) {
		return this.loadPlayer(userName);
	}

	@Override
	public void onShowWindow(WindowController.WindowName window) {
		switch (window) {
			case REGISTER -> this.registerController.show();
			case MODE -> this.modeController.show();
			case SOCIAL_PANEL -> this.socialPanelController.show();
			case GAME_MODE -> this.gameModeController.show();
			case TEAM -> this.teamController.show();
			case FLOOR -> this.floorController.show();
			case WAIT -> this.waitWindowController.show();
			case CONFIRM_TEAM -> this.confirmTeamController.show();
			case BATTLE -> {
				this.switchToBattleWindow(); //TODO: implement battleWindowDifferently
			}
			case LOAD_TEAM_PANEL -> this.loadTeamPanelController.show();
			case NEXT_ROOM -> this.nextRoomController.show();
			case LEVEL_UP -> this.levelUpController.show();
			case FLOOR_REWARD -> this.floorRewardController.show();
			case CHOOSE_BUGEMEON -> {
				try {
					this.chooseBugemonController.show();
				} catch (ViewLoadException e) {}
			}
		}
	}

	@Override
	public void onSetGameMode(GameMode gameMode) {
		this.setGameMode(gameMode);
	}

	@Override
	public PlayerDTO onGetPlayer() {
		return this.getPlayer();
	}

	@Override
	public void onSetNewTimeLine(EventHandler waitCycle) { this.waitWindowController.setNewTimeLine(waitCycle); }

	@Override
	public void onStopWaitWindow() { this.waitWindowController.stop(); }

	@Override
	public void onConfirmTeamSetter(List<BugemonDTO> team) {
		this.confirmTeamController.setPlayerTeam(team);
		this.confirmTeamController.setGameMode(this.gameMode);
	}

	@Override
	public void onSetPlayer(PlayerDTO player) { this.player = player; }

	@Override
	public GameMode onGetGameMode() { return this.gameMode; }

	/**
	 * Sends the player's team to the server and switches to the battle mode window.
	 */
	@Override
	public void setupTeamAndShowConfirmTeam(List<BugemonDTO> teamDTO) {
		this.player.setTeam(teamDTO);
		if (!this.postData(new SetUpTeamMessage(teamDTO))){
			return;
		}
		this.confirmTeamController.setGameMode(this.gameMode);
		this.confirmTeamController.setPlayerTeam(teamDTO);
	
		this.confirmTeamController.show();
	}

//	@Override
//	public void onShowBattleEnd(boolean victory, int totalXp, String opponent) {
//		try {
//
//			this.battleEndController.show(victory, totalXp, opponent);
//		} catch (ViewLoadException e) {}
//	}

	@Override
	public void onNextRoom() { this.nextRoom(); }
}