package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ulb.communication.SocketClient;
import ulb.communication.old_types.TowerInfoMessage;
import ulb.communication.types.GameMode;
import ulb.controller.windows.BattleEndController;
import ulb.controller.windows.TeamController;
import ulb.message.ClientToServerMessage;
import ulb.message.clientToServer.*;
import ulb.message.serverToClient.*;
import ulb.message.serverToClient.NextWindowMessage.WindowType;
import ulb.model.battle.BattleState;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.item.ItemDTO;
import ulb.repository.LoadException;


public class ClientController extends Application implements RegisterController.Listener, ModeController.Listener,
BattleModeController.Listener, BattleWindowController.Listener, NextRoomController.Listener {
    SocketClient client;
    Stage stage;

	PlayerDTO player;
	GameMode gameMode;
	BattleController battleController;

	RegisterController registerController;
    ModeController modeController;
	TeamController teamController;
	BattleModeController battleModeController;
	BattleWindowController battleWindowController;

	BattleEndController battleEndController;
	NextRoomController nextRoomController;

    @Override
    public void init(){
        List<String> params = getParameters().getRaw();

        String serverIp = params.get(0);
        Integer serverPort = Integer.parseInt(params.get(1));
        
        this.client = new SocketClient(serverIp, serverPort);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
		
		this.stage = primaryStage;

        Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
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

	public boolean postData(ClientToServerMessage message){
		client.sendMessage(message);
		if (client.receiveMessage() instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
			return false;
		}
		return true;
	}

	public Serializable getData(ClientToServerMessage message){
		client.sendMessage(message);
		return client.receiveMessage();
	}

	public boolean logIn(PlayerDTO player){
		return postData(new RegisterMessage(player, true));
	}

	public boolean signUp(PlayerDTO player){
		return postData(new RegisterMessage(player, false));
	}

	// Bye Bye

	public PlayerDTO getPlayer(){ return this.player; }
	public List<BugemonDTO> getPlayerTeam(){ return this.player.getTeam(); }
	public void setPlayerTeam(List<BugemonDTO> playerTeam){ this.player.setTeam(playerTeam);}
	public void setPlayer(PlayerDTO player){this.player=player; }
	public void showModeController(){
		try {this.modeController.show();}
	catch (Exception e){}}
	public void showTeamController(){this.teamController.show();}

	// Register Controller :

	@Override
	public void onLogin(String username, String password){
		try {
			this.player = new PlayerDTO(username, password, new ArrayList<>(), new HashMap<>());
			boolean success = logIn(this.player);
			if (success) {
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

	@Override
	public void onSignUp(String username, String password){
		try {
			this.player = new PlayerDTO(username, password, new ArrayList<>(), new HashMap<>());
			boolean success = this.signUp(this.player);
			if (success) {
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
			this.registerController.getView().setErrorLabel("Nom d'utilisateur ou mot de passe incorrect.");
		}
	}


	// Mode Controller Listener :

	@Override
	public void onSolo() {
		this.teamController = new TeamController(this.stage, this);
		try {
			this.teamController.show();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void onMultiplayer() {

	}

	// Team Controller Listener :



	// Battle Mode Controller Listener : 

	// private BattleController getCurrentBattleController() {
	// 	if (gameMode == GameMode.TOWER && towerModeTowerManager != null) {
	// 		return towerModeTowerManager.getCurrentBattleController();
	// 	}
	// 	return normalModeBattleController;
	// }

	private void switchToNextRoomWindow(boolean hasFled){
		this.nextRoomController = new NextRoomController(stage, this);
		try{
			this.nextRoomController.show(hasFled);
		} catch (Exception e){
			System.err.println(e);
		}
		
	}

	private void switchToBattleWindow() {
		int towerFloorNumber = 0, towerRoomNumber = 0;
		if (this.gameMode == GameMode.TOWER){
			if (this.getData(new GetTowerInfoMessage()) instanceof TowerInfoMessage towerInfo){
				towerFloorNumber = towerInfo.getFloorNumber();
				towerRoomNumber = towerInfo.getRoomNumber();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void switchToLevelUpWindow(){
		Serializable message = getData(new GetBattleEndInfoMessage());
		boolean victory = false;
		int totaleXp = 0;
		if (message instanceof BattleEndInfoMessage battleInfo){
			victory = battleInfo.isVictory();
			totaleXp = battleInfo.getTotalXp();
		} else {
			return;
		}

		this.battleEndController = new BattleEndController(stage, this);
		battleEndController.show(victory, totaleXp);
	}

	private void switchToTowerRewardWindow(){
		System.out.println("SWITCHING TO REWARD WINDOW");
	}


	public void nextRoom(){
		WindowType nextWindow = this.getWindowType();
		switch (nextWindow) {
			case NEXT_ROOM:
				switchToNextRoomWindow(false);
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
				showModeController();
				break;

			default:
				break;
		}
		
	}

	@Override
	public void onAutoBattle() {
		this.gameMode = GameMode.AUTO;
		if (this.postData(new SetUpNormalModeMessage())){
			switchToBattleWindow();
		}
	}

	@Override
	public void onControlledBattle() {
		this.gameMode = GameMode.CONTROLLED;
		if (this.postData(new SetUpNormalModeMessage())){
			switchToBattleWindow();
		}
	}

	@Override
	public void onTowerMode() {
		this.gameMode = GameMode.TOWER;
		if (this.postData(new SetUpTowerModeMessage())){
			switchToBattleWindow();
		}
	}

	@Override
	public void onReturnToCreateTeamWindow() {
		try {
			teamController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Battle Window Controller Listener : 

	@Override
	public void onBattleStateChecked(BattleState state, ActionEvent event) {
		//CLIENT
		if (state != BattleState.WON && state != BattleState.LOST){
			return;
		}

		nextRoom();
		//handleBattleEndCheckMessage(new BattleEndCheckMessage(state, event))
	}
	
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

	@Override
	public BattleState getState(){
		Serializable message = getData(new GetBattleStateMessage());

		if (message instanceof BattleStateMessage battleState){
			System.out.println("State  = " + battleState.getBattleState());
			return battleState.getBattleState();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;

	}

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

	@Override
	public Map<ItemDTO, Boolean> checkItems(List<ItemDTO> items){
		Serializable message = getData(new CheckUsableItemMessage(items));

		if (message instanceof UsableItemsMessage usableItems){
			return usableItems.getItemMap();
		} else if (message instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
		}
		return null;
	}

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

	@Override
	public BattleState onAutoTurn() {
		// CLIENT
		if (!postData(new PickRandomActionMessage())){
			return null;
		}
		return getState();
	}

	@Override
	public BattleState onUseItem(ItemDTO item) {
		if (!postData(new UseItemMessage(item))){
			return null;
		}
		return getState();
	}

	@Override
	public BattleState onSwapBugemon(BugemonDTO bugemon) {
		if (!postData(new SwapBugemonMessage(bugemon))){
			return null;
		}
		return getState();
	}

	@Override
	public BattleState onUseAbility(AbilityDTO ability) {
		if (!postData(new UseAbilityMessage(ability))){
			return null;
		}
		return getState();
	}

	@Override
	public void onRun() {
		if (postData(new RunMessage())){
			if (this.gameMode == GameMode.TOWER){
				switchToNextRoomWindow(true);
			} else {
				nextRoom();
			}
		}
	}

	public WindowType getWindowType(){
		Serializable message = getData(new GetNextWindowMessage());

		if (message instanceof NextWindowMessage nextWindow){
			return nextWindow.getNextWindow();
		}

		return null;
	}

	// Next Room Listener 

	@Override
	public void onContinue() {
		nextRoom();
	}

	@Override
	public void onReturn() {
		showModeController();
	}

}
