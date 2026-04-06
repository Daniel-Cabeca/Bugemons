package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.FloorRewardWindow;


public class FloorRewardController implements FloorRewardWindow.ViewListener {

    private FloorRewardWindow view;
    private Stage stage;
    private final Listener listener;

    private final int floor;
    private final int room;

    public FloorRewardController(Stage stage, Listener listener, int floor, int room) {
        this.stage = stage;
        this.listener = listener;
        this.floor = floor;
        this.room = room;
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.FLOOR_REWARD));
        loader.load();

        view = loader.getController();
        view.setViewListener(this);
        view.initializeLabels(floor, room);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    @Override
    public void onObjectReward() {
        listener.onObjectReward();
    }

    @Override
    public void onAttackReward() {
        listener.onAttackReward();
    }

    @Override
    public void onStatReward() {
        listener.onStatReward();
    }

    public interface Listener {
        void onObjectReward();
        void onAttackReward();
        void onStatReward();
    }
}
