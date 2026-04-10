package ulb.controller.windows;

import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.controller.TeamController;
import ulb.model.Player;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

public class ModeController extends WindowController<ModeWindow> implements ModeWindow.ViewListener {

    private TeamController.Listener teamListener;
    private Player player;

    public ModeController(Stage stage, TeamController.Listener teamListener, Player player) {
        super(stage, WindowPath.MODE);
        this.teamListener = teamListener;
        this.player = player;
        getView().setListener(this);
    }

    @Override
    public void onSolo() {
        TeamController teamController = new TeamController(getStage(), teamListener, player);
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
