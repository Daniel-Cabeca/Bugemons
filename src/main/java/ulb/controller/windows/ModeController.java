package ulb.controller.windows;

import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.controller.TeamController;
import ulb.model.Player;
import ulb.service.BugemonService;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

public class ModeController extends WindowController<ModeWindow> implements ModeWindow.ViewListener {

    private TeamController.Listener teamListener;
    private Player player;
	private final BugemonService bugemonService;

    public ModeController(Stage stage, TeamController.Listener teamListener, Player player, BugemonService bugemonService) {
        super(stage, WindowPath.MODE);
        this.teamListener = teamListener;
        this.player = player;
		this.bugemonService = bugemonService;
        getView().setListener(this);
    }

	BugemonService getBugemonService() {
		return this.bugemonService;
	}

    @Override
    public void onSolo() {
        TeamController teamController = new TeamController(getStage(), teamListener, player, this.getBugemonService());
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
