package ulb.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.view.WindowPath;
import ulb.view.windows.BattleEndWindow;

public class BattleEndController implements BattleEndWindow.ViewListener {
    private Listener listener;
    private Stage stage;
    private BattleEndWindow view;

    public BattleEndController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    public void show(boolean victory, int totalXP) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.BATTLE_END));
        loader.load();
        view = loader.getController();
        this.view.setViewListener(this);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.view.setResult(victory, totalXP);
        this.stage.show();
    }


    @Override
    public void onHandleReturn() {
        this.listener.onHandleReturn();
    }

    public interface Listener{
        void onHandleReturn();
    }
}
