package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.FloorWindow;

public class FloorController implements FloorWindow.ViewListener {
    private Listener listener;
    private Stage stage;
    private FloorWindow view;

    public FloorController(Stage stage, Listener listener){
        this.stage = stage;
        this.listener = listener;
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.FLOOR));
        loader.load();
        view = loader.getController();
        view.setViewListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    @Override
    public void onBoss() {
        this.listener.onBoss();
    }

    @Override
    public void onBattleA1() {
        this.listener.onBattleA1();
    }

    @Override
    public void onBattleA2() {
        this.listener.onBattleA2();
    }

    @Override
    public void onBattleB() {
        this.listener.onBattleB();
    }

    @Override
    public void onBonusA() {
        this.listener.onBonusA();
    }

    @Override
    public void onBonusB() {
        this.listener.onBonusB();
    }

    @Override
    public void onPlayer() {
        this.listener.onPlayer();
    }

    interface Listener{
        void onBoss();
        void onBattleA1();
        void onBattleA2();
        void onBattleB();
        void onBonusA();
        void onBonusB();
        void onPlayer();
    }
}
