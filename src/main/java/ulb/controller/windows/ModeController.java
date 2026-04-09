package ulb.controller.windows;


import javafx.application.Platform;
import ulb.controller.GameController;
import ulb.controller.TeamController;
import ulb.model.Player;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

public class ModeController extends WindowController implements ModeWindow.ViewListener {

    private TeamController teamController;
    private Player player;
    private GameController gameController;


    public ModeController() {
        setWindowPath(WindowPath.MODE);
        try {
            this.init();
        } catch (Exception e) {
            System.err.println("Couldn't load the FXML file");
        }

        ((ModeWindow)this.getView()).setListener(this);

    }

    public void setPlayer(Player player) { // TO REMOVE
        this.player = player;
    }
    public void setTeamController(TeamController teamController) {
        this.teamController = teamController;
    }
    public void setGameController(GameController gameController) {this.gameController = gameController;} // TO REMOVE


    @Override
    public void onSolo() {
        teamController = new TeamController(getStage(),this.gameController, player); // TO REMOVE
        try {
            teamController.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void quit() {
        Platform.exit();
        System.exit(0);
    }

}
