package ulb.controller;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import ulb.communication.Client;
import ulb.communication.Message;
import ulb.communication.types.BugemonSpeciesMessage;
import ulb.communication.types.ErrorMessage;
import ulb.communication.types.GameMode;
import ulb.communication.types.GetAllBugemonSpeciesMessage;
import ulb.communication.types.SetUpNormalModeMessage;
import ulb.communication.types.SetUpPlayerMessage;
import ulb.communication.types.SetUpTeamMessage;
import ulb.communication.types.SetUpTowerModeMessage;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.model.bugemon.Bugemon;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.item.ItemDTO;

public class ClientController extends Application implements ModeController.Listener, TeamController.Listener, 
BattleModeController.Listener {
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
		// BattleController battleController = getCurrentBattleController();
		// int towerFloorNumber = gameMode == GameMode.TOWER ? getTowerFloorNumber() : 0;
		// int currentRoomIndex = gameMode == GameMode.TOWER ? getCurrentRoomIndex() : 0;
		// battleWindowController = new BattleWindowController(
		// 		this.stage,
		// 		this,
		// 		player,
		// 		battleController,
		// 		gameMode,
		// 		towerFloorNumber,
		// 		currentRoomIndex
		// );

		// try {
		// 	battleWindowController.show();
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
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

}
