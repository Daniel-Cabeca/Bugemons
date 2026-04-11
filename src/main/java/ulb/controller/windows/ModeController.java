package ulb.controller.windows;


import javafx.application.Platform;
import javafx.stage.Stage;
import ulb.DTO.player.PlayerDTO;
import ulb.controller.ClientController;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

public class ModeController extends WindowController<ModeWindow> implements ModeWindow.ViewListener {

    private ClientController clientController;


    public ModeController(Stage stage, ClientController clientController) {
        this.stage = stage;
        this.clientController = clientController;
        this.windowPath = WindowPath.MODE;

        try {
            this.init();
        } catch (Exception e) {
            System.err.println("Couldn't load the FXML file : " + e);
        }
        this.view.setListener(this);
    }


    @Override
    public void onSolo() {
        try {
            this.clientController.getTeamController().show();
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
