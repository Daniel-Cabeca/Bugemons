package ulb.controller;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.List;

import ulb.communication.Client;

public class ClientController extends Application implements ModeController.Listener{
    Client client;
    Stage currenStage;

    ModeController modeController;

    @Override
    public void init(){
        List<String> params = getParameters().getRaw();

        String serverIp = params.get(0);
        Integer serverPort = Integer.parseInt(params.get(1));
        
        this.client = new Client(serverIp, serverPort);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
		// CLIENT
		// teamController = new TeamController(stage, this, player);
		// try {
		// 	teamController.show();
		// } catch (Exception e) {
		// 	e.printStackTrace();
		// }
        return;
	}
}
