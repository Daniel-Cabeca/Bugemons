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
import ulb.communication.types.GetAllBugemonSpeciesMessage;
import ulb.communication.types.SetUpPlayerMessage;
import ulb.mapper.bugemon.BugemonMapper;
import ulb.model.bugemon.Bugemon;
import ulb.DTO.bugemon.BugemonDTO;
import ulb.DTO.bugemon.BugemonSpeciesDTO;
import ulb.DTO.player.PlayerDTO;
import ulb.DTO.item.ItemDTO;

public class ClientController extends Application implements ModeController.Listener, TeamController.Listener{
    Client client;
    Stage currenStage;

	PlayerDTO player;

    ModeController modeController;
	TeamController teamController;

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
		
		this.currenStage = primaryStage;

        Font.loadFont(getClass().getResourceAsStream("/fonts/pokemon-emerald-pro.otf"), 14);
		primaryStage.setTitle("INFO-F307 Groupe 10");
		primaryStage.setFullScreen(true);
		primaryStage.setFullScreenExitHint("");

		this.modeController = new ModeController(this.currenStage, this);
		this.modeController.show();

		if (primaryStage.getScene() != null) {
			String stylesheet = getClass().getResource("/styles/global.css").toExternalForm();
			if (!primaryStage.getScene().getStylesheets().contains(stylesheet)) {
				primaryStage.getScene().getStylesheets().add(stylesheet);
			}
		}

		primaryStage.show();
    }

    @Override
	public void onSolo() {
		this.teamController = new TeamController(this.currenStage, this, this.player);
		try {
			this.teamController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return;
	}

	@Override
	public void onReturn() {
		// CLIENT
		try {
			modeController.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<BugemonSpeciesDTO> getAllSpecies(){
		client.sendMessage(new GetAllBugemonSpeciesMessage());
		Message message = client.receiveMessage();
		if (message instanceof BugemonSpeciesMessage speciesMessage){
			return speciesMessage.getSpecies();
		}
		return null;
	}

	@Override
	public void onTeamConfirmed() {
		// CLIENT + SERVER
		// List<BugemonDTO> team = new ArrayList<BugemonDTO>();
		// for (Bugemon bugemon : getTeam().getMembers()){
		// 	team.add(BugemonMapper.toDTO(bugemon));
		// }
		// battleModeController = new BattleModeController(stage, this, team);
		// try {
		// 	battleModeController.show();
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
		return;
	}

}
