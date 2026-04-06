package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.NextRoomWindow;

public class NextRoomController implements NextRoomWindow.ViewListener {

    private final Listener listener;
    private NextRoomWindow view;
    private Stage stage;

    public NextRoomController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    public void show(boolean hasFled) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.NEXT_ROOM));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);

        view.displayMessage(hasFled);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    @Override
    public void onContinue() {
        listener.onContinue();
    }

    @Override
    public void onReturn() {
        listener.onReturn();
    }

    public interface Listener {
        void onContinue();
        void onReturn();
    }
}
