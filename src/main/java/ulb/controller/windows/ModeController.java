package ulb.controller.windows;


import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

public class ModeController extends WindowController<ModeWindow> implements ModeWindow.ViewListener {

    public ModeController(Stage stage, ClientController clientController) {
        super(stage, WindowPath.MODE, clientController);
        this.view.setListener(this);
    }


    @Override
    public void onSolo() {
        try {
            this.clientController.showTeamController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMultiplayer() {
        //
    }

    @Override
    public void onQuit() {
        Platform.exit();
        System.exit(0);
    }

}
