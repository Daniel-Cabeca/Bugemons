package ulb.controller;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.ModeWindow;

public class ModeController implements ModeWindow.ViewListener {

    private Listener listener;
    private Stage stage;
    private ModeWindow view;

    public ModeController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.MODE));
        loader.load();
        view = loader.getController();
        view.setListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    @Override
    public void onOpenSocial() {
        listener.onOpenSocial();
    }

    @Override
    public void onSolo() {
        listener.onSolo();
    }

    @Override
    public void onMultiplayer() {
        listener.onMultiplayer();
    }

    @Override
    public void onQuit() {
        Platform.exit();
        System.exit(0);
    }

    public interface Listener {
        void onOpenSocial();
        void onSolo();
        void onMultiplayer();
    }
}
