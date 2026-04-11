package ulb.controller.windows;


import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.controller.TeamController;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

public class ModeController extends WindowController<ModeWindow> implements ModeWindow.ViewListener {

    private TeamController teamController;
    private PlayerDTO player;
    private ClientController clientController;


    public ModeController(Stage stage) {
        this.stage = stage;
        this.windowPath = WindowPath.MODE;

        try {
            this.init();
        } catch (Exception e) {
            System.err.println("Couldn't load the FXML file : " + e);
        }
        this.view.setListener(this);
    }

    public void setPlayer(PlayerDTO player) { // TO REMOVE
        this.player = player;
    }
    public void setTeamController(TeamController teamController) {
        this.teamController = teamController;
    }
    public void setClientController(ClientController clientController) {this.clientController = clientController;} // TO REMOVE


    @Override
    public void onSolo() {
        teamController = new TeamController(this.stage, this.clientController, this.player);
        try {
            teamController.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onQuit() {
        Platform.exit();
        System.exit(0);
    }

}
