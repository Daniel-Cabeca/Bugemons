package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import ulb.communication.SocketClient;
import ulb.controller.windows.*;
import ulb.controller.windows.Battle.BattleWindowController;
import ulb.exceptions.CommunicationException;
import ulb.exceptions.ViewLoadException;
import ulb.communication.GameMode;
import ulb.message.request.*;
import ulb.message.response.*;
import ulb.message.request.gameActions.*;
import ulb.message.request.gameInfo.*;
import ulb.message.request.playerInfo.*;
import ulb.message.request.setup.*;
import ulb.message.response.gameInfo.*;
import ulb.message.response.playerInfo.*;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.reward.RewardDTO;

/**
 * Client-side application controller coordinating server messaging.
 */
public class ClientController extends Application {

	private static final Logger LOGGER = Logger.getLogger(ClientController.class.getName());

	SocketClient client;
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
		this.battleWindowController = new BattleWindowController(this.stage, this);
		this.floorRewardController = new FloorRewardController(this.stage, this);
		this.battleEndController = new BattleEndController(this.stage, this);
		this.chooseBugemonController = new ChooseBugemonController(this.stage, this);
		this.attackReplacementController = new AttackReplacementController(this.stage, this);
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
	 * @param request The request sent to the server
	 * @return True if the request was accepted by the server
	 */
	public boolean postData(Request request){
		Response response = this.client.getResponse(request);
		return response.isSuccess();
	}

	/**
	 * Sends a request to the server and returns the response.
	 *
	 * @param request The message sent to the server
	 * @return The response received from the server
	 */
	public Response getData(Request request){
		return this.client.getResponse(request);
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
		if (getData(new GetPlayerRequest(username)) instanceof PlayerResponse msg) {
			return msg.getPlayer();
		}
		return null;
	}

	public void unsetPlayer() { this.player = null; }

	/**
	 * Loads a player by username, stores it as the current player, and returns it.
	 *
	 * @param userName The username to look up
	 * @return The player DTO, or null if not found
	 */
	public PlayerDTO loadPlayer(String userName) {
		this.player = this.getPlayer(userName);
		return this.player;
	}

	public void setGameMode(GameMode gameMode){this.gameMode = gameMode;}

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
		this.battleWindowController.setPlayer(player);
		this.battleWindowController.setGameMode(gameMode);
		this.battleWindowController.show();
	}

	/**
	 * Switches to the battle end window with battle result.
	 */
	private void switchToBattleEndWindow(){
		Serializable message = getData(new GetBattleEndInfoRequest());
		boolean victory;
		int totalXp;
		String opponent;
		boolean multiplayerBattle;
		if (message instanceof BattleEndInfoResponse battleInfo){
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
	 * Switches to the next window according to info gotten from the server.
	 */
	public void nextRoom(){
		WindowType nextWindow = this.getWindowType();
		if (nextWindow == null) {
			LOGGER.warning("Impossible to determine the next window from the server.");
			return;
		}

		switch (nextWindow) {
			case NEXT_ROOM:
				switchToNextRoomWindow();
				break;

			case GAME:
				switchToBattleWindow();
				break;

			case LEVEL_UP:
				this.levelUpController.show();
				break;

			case REWARD:
				this.floorRewardController.show();
				break;

			case MAIN_MENU:
				switchToBattleEndWindow();
				break;

			case FLOOR:
				this.floorController.show();
				break;

			default:
				break;
		}
	}

	/**
	 * Returns the next window type according to server flow.
	 *
	 * @return The next window type, or null if unavailable
	 */
	public WindowType getWindowType(){
		Serializable message = getData(new GetNextWindowRequest());

		if (message instanceof NextWindowResponse nextWindow){
			return nextWindow.getNextWindow();
		} else if (message instanceof StatusResponse errorMessage && errorMessage.isFailure()) {
			LOGGER.warning("Failed to get next window: " + errorMessage.getMessage());
		}

		return null;
	}

	public void chooseReward(RewardDTO reward, ActionEvent event) {
		if (postData(new ChooseLevelUpRewardRequest(reward))) {
			nextRoom();
		}
	}

	// Floor Reward Listener

	public void chooseBugemonReward(RewardChoice rewardChoice) {
		pendingFloorRewardChoice = rewardChoice;
		if (chooseBugemonController == null) {
			chooseBugemonController = new ChooseBugemonController(this.stage, this);
		}
		chooseBugemonController.show();
	}

	/**
	 * Returns the current floor number and room number from the server.
	 */
	public List<Integer> getTowerInfo() {
		if (getData(new GetTowerInfoRequest()) instanceof TowerInfoResponse towerInfo){
			return List.of(towerInfo.getFloorNumber(), towerInfo.getRoomNumber());
		}
		return null;
	}

	// FloorController

	public void setOpponentMulti(PlayerDTO opponent) {
		this.teamController.setOpponent(opponent);
	}

	public void showWindow(WindowController.WindowName window) {
		switch (window) {
			case REGISTER -> this.registerController.show();
			case MODE -> this.modeController.show();
			case SOCIAL_PANEL -> this.socialPanelController.show();
			case GAME_MODE -> this.gameModeController.show();
			case TEAM -> this.teamController.show();
			case FLOOR -> this.floorController.show();
			case WAIT -> this.waitWindowController.show();
			case CONFIRM_TEAM -> this.confirmTeamController.show();
			case BATTLE -> this.switchToBattleWindow();
			case LOAD_TEAM_PANEL -> this.loadTeamPanelController.show();
			case NEXT_ROOM -> this.nextRoomController.show();
			case LEVEL_UP -> this.levelUpController.show();
			case FLOOR_REWARD -> this.floorRewardController.show();
			case CHOOSE_BUGEMON -> this.chooseBugemonController.show();
		}
	}

	/**
	 * Set newTimeLine in WaitWindow
	 * @param waitCycle is current waitCycle
	 */
	public void setNewTimeLine(EventHandler waitCycle) { this.waitWindowController.setNewTimeLine(waitCycle); }

	public void stopWaitWindow() { this.waitWindowController.stop(); }

	/**
	 * Sends the player's team to the server and switches to the battle mode window.
	 */
	public void setupTeamAndShowConfirmTeam(List<BugemonDTO> teamDTO) {
		this.player.setTeam(teamDTO);
		if (!this.postData(new SetUpTeamRequest(teamDTO))){
			return;
		}
		this.confirmTeamController.setGameMode(this.gameMode);
		this.confirmTeamController.setPlayerTeam(teamDTO);
	
		this.confirmTeamController.show();
	}

	public void showAttackReplacement(BugemonDTO bugemon, AbilityDTO newAbility) {
		this.attackReplacementController.show(bugemon, newAbility);
	}

	public RewardChoice getPendingFloorRewardChoice() { return this.pendingFloorRewardChoice; }
}