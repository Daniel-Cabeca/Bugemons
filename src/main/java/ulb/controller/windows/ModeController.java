package ulb.controller.windows;


import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ulb.controller.ClientController;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;
import ulb.view.windows.SocialPanel;

public class ModeController extends WindowController<ModeWindow> implements ModeWindow.ViewListener {

    public ModeController(Stage stage, ClientController clientController) {
        super(stage, WindowPath.MODE, clientController);
        this.view.setListener(this);
    }


    @Override
    public void onOpenSocial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.SOCIAL_PANEL));
            Parent root = loader.load();
            SocialPanel panel = loader.getController();
            Stage popup = new Stage();
            popup.initStyle(StageStyle.UNDECORATED);
            popup.initOwner(stage);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/global.css").toExternalForm());
            popup.setScene(scene);
            panel.setStage(popup);
            popup.setX(stage.getX());
            popup.setY(stage.getY());
            popup.show();
            panel.setClientController(clientController);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
