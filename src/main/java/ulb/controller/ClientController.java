package ulb.controller;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ulb.communication.Client;
import ulb.communication.Message;
import ulb.communication.old_types.BattleEndCheckMessage;
import ulb.communication.old_types.TowerInfoMessage;
import ulb.communication.types.ActiveBugemonsMessage;
import ulb.communication.types.BugemonSpeciesMessage;
import ulb.communication.types.ErrorMessage;
import ulb.communication.types.GameMode;
import ulb.communication.types.GetActiveBugemonsMessage;
import ulb.communication.types.GetAllBugemonSpeciesMessage;
import ulb.communication.types.GetTowerInfoMessage;
import ulb.communication.types.SetUpNormalModeMessage;
import ulb.communication.types.SetUpPlayerMessage;
import ulb.communication.types.SetUpTeamMessage;
import ulb.communication.types.SetUpTowerModeMessage;
import ulb.controller.action.Swap;
import ulb.controller.action.UseAbility;
import ulb.controller.action.UseItem;
import ulb.controller.strategy.StrategyRandom;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.model.battle.BattleState;

import ulb.DTO.ability.AbilityDTO;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.item.ItemDTO;

public class ClientController extends Application implements ModeController.Listener, TeamController.Listener, 
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

		this.modeController = new ModeController(this.stage, this);
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
		if (client.receiveMessage() instanceof ErrorMessage errorMessage){
			System.err.println(errorMessage.getError());
			return false;
		}
		return true;
	}

	private Message getData(Message message){
		client.sendMessage(message);
		return client.receiveMessage();
	}

	// Mode Controller Listener : 

    @Override
	public void onSolo() {
		this.teamController = new TeamController(this.stage, this, this.player);
		try {
			this.teamController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return;
	}

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
		Message message = this.getData(new GetAllBugemonSpeciesMessage());

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
		Message message = getData(new GetActiveBugemonsMessage());
		if (message instanceof ActiveBugemonsMessage activeBugemons){
			return List.of(activeBugemons.getSelfActiveBugemon(), activeBugemons.getOpponentActiveBugemon());
			
		} else if (message instanceof ErrorMessage errorMessage){
			System.err.println(errorMessage.getError());
		}
		return null;
	}

	@Override
	public String getAbilityEffectiveness(AbilityDTO ability, BugemonDTO bugemon){return null;}
	@Override
	public List<Integer> getHpAfterFirstAction(){return null;}
	@Override
	public BattleState getState(){return null;}
	@Override
	public List<String> getLogs(){return null;}
	@Override
	public boolean checkItem(ItemDTO item){return false;}
	@Override
	public boolean isGameFinished(){return false;}

	@Override
	public BattleState onAutoTurn() {
		// SERVER
		// StrategyRandom strategyRandom = new StrategyRandom(normalModeBattleController);
		// return strategyRandom.playAutoTurn();
		return null;
	}

	@Override
	public BattleState onUseItem(ItemDTO item) {
		// CLIENT + SERVER
		// BattleController battleController = battleControllerForManualTurn();
		// if (battleController != null && item != null) {
		// 	battleController.useAction(new UseItem(item));
		// 	return battleController.getState();
		// }
		return null;
	}

	@Override
	public BattleState onSwapBugemon(BugemonDTO bugemon) {
		// CLIENT + SERVER
		// BattleController battleController = battleControllerForManualTurn();
		// if (battleController != null && bugemon != null) {
		// 	battleController.useAction(new Swap(bugemon));
		// 	return battleController.getState();
		// }
		return null;
	}

	@Override
	public BattleState onUseAbility(AbilityDTO ability) {
		// BattleController battleController = battleControllerForManualTurn();
		// if (battleController != null && ability != null) {
		// 	battleController.useAction(new UseAbility(ability));
		// 	return battleController.getState();
		// }
		return null;
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
