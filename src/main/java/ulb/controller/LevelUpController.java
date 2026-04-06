package ulb.controller;

import ulb.view.windows.LevelUpWindow;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ulb.model.bugemon.Bugemon;
import ulb.model.reward.Reward;
import ulb.view.WindowPath;

public class LevelUpController implements LevelUpWindow.ViewListener{


    private final Listener listener;
    private LevelUpWindow view;
    private Stage stage;

    public LevelUpController(Stage stage, Listener listener) {
        this.stage = stage;
        this.listener = listener;
    }

    public void show() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(WindowPath.LEVEL_UP));
        loader.load();

        view = loader.getController();
        view.setViewListener(this);

        Bugemon bugemon = listener.getLevelUpBugemon();
        List<Reward> rewards = listener.getLevelUpRewards();

        view.initializeView(bugemon, rewards);

        Parent root = loader.getRoot();
        if (stage.getScene() == null) {
            stage.setScene(new Scene(root));
        } else {
            stage.getScene().setRoot(root);
        }
        this.stage.show();
    }

    @Override
    public void onRewardChosen(Reward reward, ActionEvent event) {
        listener.onRewardChosen(reward, event);
    }

    public interface Listener {
        Bugemon getLevelUpBugemon();
        List<Reward> getLevelUpRewards();
        void onRewardChosen(Reward reward, ActionEvent event);
    }
}
