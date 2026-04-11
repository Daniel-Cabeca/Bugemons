package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ulb.communication.Client;
import ulb.communication.Message;
import ulb.communication.old_types.TowerInfoMessage;
import ulb.communication.message.serverToClient.AbilityEffectivenessMessage;
import ulb.communication.message.serverToClient.ActiveBugemonsMessage;
import ulb.communication.message.serverToClient.BattleStateMessage;
import ulb.communication.message.serverToClient.BugemonSpeciesMessage;
import ulb.communication.message.clientToServer.CheckGameFinishedMessage;
import ulb.communication.message.clientToServer.CheckUsableItemMessage;
import ulb.communication.message.serverToClient.StatusMessage;
import ulb.communication.message.serverToClient.GameFinishedMessage;
import ulb.communication.types.GameMode;
import ulb.communication.message.clientToServer.GetAbilityEffectivenessMessage;
import ulb.communication.message.clientToServer.GetActiveBugemonsMessage;
import ulb.communication.message.clientToServer.GetAllBugemonSpeciesMessage;
import ulb.communication.message.clientToServer.GetBattleStateMessage;
import ulb.communication.message.clientToServer.GetLogsMessage;
import ulb.communication.message.clientToServer.GetTowerInfoMessage;
import ulb.communication.message.serverToClient.LogsMessage;
import ulb.communication.message.clientToServer.PickRandomActionMessage;
import ulb.communication.message.clientToServer.SetUpNormalModeMessage;
import ulb.communication.message.clientToServer.SetUpPlayerMessage;
import ulb.communication.message.clientToServer.SetUpTeamMessage;
import ulb.communication.message.clientToServer.SetUpTowerModeMessage;
import ulb.communication.message.clientToServer.SwapBugemonMessage;
import ulb.communication.message.serverToClient.UsableItemsMessage;
import ulb.communication.message.clientToServer.UseAbilityMessage;
import ulb.communication.message.clientToServer.UseItemMessage;
import ulb.controller.windows.ModeController;
import ulb.model.battle.BattleState;
import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.item.ItemDTO;

public class ClientController extends Application implements TeamController.Listener,
BattleModeController.Listener, BattleWindowController.Listener {
    Client client;
    Stage stage;

	PlayerDTO player;
	GameMode gameMode;
	BattleController battleController;

    ModeController modeController;
	TeamController teamController;
	BattleModeController battleModeController;
	BattleWindowController battleWindowController;

    @Override
    public void init(){
        List<String> params = getParameters().getRaw();

        String serverIp = params.get(0);
        Integer serverPort = Integer.parseInt(params.get(1));
        
        this.client = new Client(serverIp, serverPort);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
		this.player = new PlayerDTO("player", new ArrayList<BugemonDTO>(), new HashMap<ItemDTO, Integer>());
        client.sendMessage(new SetUpPlayerMessage(player));
		
		this.stage = primaryStage;

        Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");

		this.modeController = new ModeController(this.stage);
		this.modeController.setClientController(this);
		this.modeController.setPlayer(this.player);
		this.modeController.show();

		if (primaryStage.getScene() != null) {
			String stylesheet = getClass().getResource("/styles/global.css").toExternalForm();
			if (!primaryStage.getScene().getStylesheets().contains(stylesheet)) {
				primaryStage.getScene().getStylesheets().add(stylesheet);
			}
		}

		primaryStage.show();
    }

	private boolean postData(Message message){
		client.sendMessage(message);
		if (client.receiveMessage() instanceof StatusMessage errorMessage && errorMessage.isFailure()){
			System.err.println(errorMessage.getMessage());
			return false;
		}
		return true;
	}

	private Serializable getData(Message message){
		client.sendMessage(message);
		return client.receiveMessage();
	}

	// Mode Controller Listener : 


	// Team Controller Listener : 

	@Override
	public void onReturn() {
		try {
			modeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies(){
		Serializable message = this.getData(new GetAllBugemonSpeciesMessage());

		if (message instanceof BugemonSpeciesMessage speciesMessage){
			return speciesMessage.getSpecies();
		}
		return null;
	}

	@Override
	public void onTeamConfirmed() {
		List<BugemonDTO> team = player.getTeam();

		if (!this.postData(new SetUpTeamMessage(team))){
			return;
		}

		this.battleModeController = new BattleModeController(this.stage, this, player.getTeam());
		try {
			battleModeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	// Battle Mode Controller Listener : 

	// private BattleController getCurrentBattleController() {
	// 	if (gameMode == GameMode.TOWER && towerModeTowerManager != null) {
	// 		return towerModeTowerManager.getCurrentBattleController();
	// 	}
	// 	return normalModeBattleController;
	// }

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
			//handleTower();
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
		// CLIENT 
		//handleBattleEndCheckMessage(new BattleEndCheckMessage(state, event));
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
	public void onTowerFlee() {
		// CLIENT + SERVER
		// handleTowerFlee();
		// switchToNextRoomWindow();
	}

	@Override
	public void onReturnToMode() {
		// CLIENT
		try {
			modeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
